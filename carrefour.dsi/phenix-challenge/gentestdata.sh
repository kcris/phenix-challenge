#!/bin/bash

#
# generate large volume of test data for phenix-challenge
#
# please configure the desired number of stores/products/transactions, see below the N_XXX variables
#
# WARNING: generation is slow, for 1200 stores, 10000 products, 15000 transactions it takes about 9-10 hours to complete
#

#configuration
DESTDIR=data/output
MAX_QUANTITY=20
MAX_PRICE=300
DATES=(20170514 20170513 20170512 20170511 20170510 20170509 20170508)
N_PRICES=100

N_STORES=1200
N_PRODUCTS=10000
N_TRANSACTIONS=15000

#prices cache - it's too slow to generate (lots of) distinct floats
arrPrices=()
for i in $(seq 1 ${N_PRICES});
do
	price=$(seq 1 .01 ${MAX_PRICE} | shuf | head -n1)
	arrPrices+=($price)
done

#generate stores
arrStoreIds=()
for i in $(seq 1 ${N_STORES});
do
	u=$(uuidgen)
	arrStoreIds+=($u)
done
printf '%s\n' "${arrStoreIds[@]}"

#generate journals
mkdir -p $DESTDIR
pushd $DESTDIR
for j in $(seq 1 ${#DATES[@]});
do
	theDate=${DATES[${j}-1]}
	
	mkdir ${theDate}
	pushd ${theDate}
	
	#generate transactions list - one csv per day (all stores in the same file)
	for i in $(seq 1 ${N_TRANSACTIONS});
	do
		nStore=$(( ( RANDOM % ${N_STORES} )  + 1 ))

		txId=$i
		txDate=xxx
		storeId=${arrStoreIds[${nStore}-1]}
		prodId=$(( ( RANDOM % ${N_PRODUCTS} )  + 1 ))
		qty=$(( ( RANDOM % $MAX_QUANTITY )  + 1 ))
		echo "$txId|$txDate|$storeId|$prodId|$qty" >> "transactions_${theDate}.data"
	done	
	
	#generate prices lists - one csv per store
	for s in $(seq 1 ${N_STORES});
	do
		storeId=${arrStoreIds[${s}-1]}
		for p in $(seq 1 ${N_PRODUCTS});
		do
			#price=$(seq 1 .01 300 | shuf | head -n1)
			
			priceIndex=$((RANDOM % ${N_PRICES}))
			price=${arrPrices[${priceIndex}]}
			
			echo "$p|$price" >> "reference_prod-${storeId}_${theDate}.data"
		done
	done
	
	popd
done
popd
