# Phenix Challenge


## Solution 1

#### In memory processing

for a given day J we have this structure called (daily) journal
 	map<storeId, productsGroup>

where productsGroup contains
	map<productId, productInfo>

where productInfo contains
	productId, unitsSold, valueSold

We'll group available input data into this structure. 

Note that:
* unitsSold - is used to determine the volume of sales for a product (ventes)
* valueSold - is used to determine the value of sales for a product (chiffre affaires)

####  Output statistics

For a given day J
- to compute top ventes/ca per store: for a given store, keep top 100 products (based on unitsSold/valueSold)
- to compute top ventes/ca globally: we *merge* all productsGroup, then keep top 100 products (based on unitsSold/valueSold)

For a given week J1..J7
- to compute top ventes/ca per store: for a given store, *merge* the 7-day productsGroup, then keep top 100 (based on unitsSold/valueSold) 
- to compute top ventes/ca globally: *merge* the 7-day productsGroup of all stores, then keep top 100 (based on unitsSold/valueSold) 

####  Conclusion

The first solution was tested with a large volume data set, about 1GB of csv data: 1200 stores, 10.000 products, 15.000 transactions.
It took about 10 hours to generate the test data set using the attached bash script.
Results on an average i7, 64bit machine: execution time about 20 seconds, 190 MB heap used, 1.7 MB stats generated.

Facts:
* we have strict memory requirements
* we have a large number of available stores/products
* we must output some aggregated statistics which must take into account product information for multiple stores (global stats), multiple days (weekly stats), or both multi-store-multi-day (global weekly stats)

Because of all this, a simple/naive in-memory solution is probably not functional under the real amount of data which can be a lot bigger.
Since we have no full test data for a real store, it's hard to do a real-world test other than based on estimations.
However, the next solution attempts to fix all this by reducing memory consumption.

## Solution 2

#### map-reduce solution using temporary files

Under the following assumptions
* we can load the journal for one day in memory
* we can load the per-product sales information (units sold, value sold) for any store/day into memory
* this matches our memory constraints

we can develop a mapped reduce solution which uses intermediary files like this
* for each {store, day} create a file which contains sales info (units sold, value sold) for all products
* we can then reduce multiple such files when aggregating statistics
	* determine a set of sales info to be merged/aggregated
	* scan/process 2 such files at a time, for each productid pId 
		* read sales info in both files
		* build a new (reduced) sales info like this: unitsSold=max(sales1.unitsSold, sales2.unitsSold), valueSold=the max(sales1.valueSold, sales2.valueSold) 
		* store the new sales info (based on merging/reducing the 2 input files) into a new file
	* repeat process until all set of sales files to be merged are reduced to one file = the final/aggregated sales info for our products
	* get the top records in our aggregated sales info (either by sales volume or sales value criteria)

note: in case we want to reduce the memory consumption even more, we can do further splitting of {store,day} sales information into smaller files having a fixed size chunk limit (a specified number of product records)

#### TODO - this is work in progress

## Technical details

- plain java application, minimal or no dependencies
	- no external libs except log4j (and that's not important either)
- large input data
	- csv is not fully loaded in memory, useless fields are ignored
	- is 'reduced' while loading to 1 entry per productId
- comparators used to parameterize the way that *top* is calculated
	- by sales volume (consider a productInfo's unitsSold)
	- by sales value (consider a productInfo's valueSold)
- aggregators supported to parameterize the way that 2 productInfos instances are merged
	- current aggregation policy: sums the unitsSold and valueSold (for the same productId)
	- useful to merge multiple productInfo coming from different stores when computing the global top
	- useful to merge weekly productInfo coming from different days (and different stores) when computing the weekly (global) top
- main work was split into 2 phases
	- phase A: create journals for last week (7 days) - this was split into 2 threads
		- one thread loads journal for 3 days
		- one thread loads journal for 4 days
	- phase B: generate the output statistics - this was also split in 2 threads
		- one thread generates volume stats daily(per store + global) and weekly (per store+global)
		- one thread generates value stats daily(per store + global) and weekly (per store+global)
	- phase B depends on phase A, they are serialized
	- the load for each phase's 2 threads is equally distributed

## TODOs

- tests
	- more unit tests
	- full-scale test data set
- optimize
	- intermediary files for smaller batch processing?
	- reuse oututs generated by previous runs?
- improve
	- configuration support (args or conf file)
	- more docs


## Running the application

```bash
cd carrefour.dsi

#re-build application via maven if needed
mvn clean package
cp phenix-challenge/target/phenix-challenge-0.0.1-SNAPSHOT.jar phenix-challenge/lib/

#run the application
java -Xmx512M -cp "phenix-challenge/lib/*" fr.carrefour.phenix.challenge.Application

#inputs taken from folder: phenix-challenge/data/input
#outputs sent to folder: phenix-challenge/data/output/<date>
```
