package fr.carrefour.phenix.challenge;

import fr.carrefour.phenix.challenge.domain.products.ProductsGroup;
import fr.carrefour.phenix.challenge.domain.products.ProductsJournal;
import fr.carrefour.phenix.challenge.domain.products.ProductsJournalsRepository;
import fr.carrefour.phenix.challenge.domain.stats.ProductsStat;
import fr.carrefour.phenix.challenge.domain.stats.daily.global.TopDailyProductsBySalesValueGlobally;
import fr.carrefour.phenix.challenge.domain.stats.daily.global.TopDailyProductsBySalesVolumeGlobally;
import fr.carrefour.phenix.challenge.domain.stats.daily.per_store.TopDailyProductsBySalesValuePerStore;
import fr.carrefour.phenix.challenge.domain.stats.daily.per_store.TopDailyProductsBySalesVolumePerStore;
import fr.carrefour.phenix.challenge.domain.stats.weekly.global.TopWeeklyProductsBySalesValueGlobally;
import fr.carrefour.phenix.challenge.domain.stats.weekly.global.TopWeeklyProductsBySalesVolumeGlobally;
import fr.carrefour.phenix.challenge.domain.stats.weekly.per_store.TopWeeklyProductsBySalesValuePerStore;
import fr.carrefour.phenix.challenge.domain.stats.weekly.per_store.TopWeeklyProductsBySalesVolumePerStore;
import fr.carrefour.phenix.challenge.model.inputs.ProductsJournalBuilder;
import fr.carrefour.phenix.challenge.model.inputs.ProductsJournalBuilderCsv;
import fr.carrefour.phenix.challenge.model.outputs.ProductsGroupSaver;
import fr.carrefour.phenix.challenge.model.outputs.ProductsGroupSaverCsv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.UUID;

class Application
{
	private static final Logger LOGGER = LogManager.getLogger(Application.class);

	//helpers to quickly generate the output filename for our statistics
	private static String CSV(String title, String storeId, LocalDate date, String suffix) {
		String subdir = String.format("%04d-%02d-%02d", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
		return String.format("%s/%s_%s_%04d%02d%02d%s.csv", subdir, title, storeId.toString(), date.getYear(), date.getMonthValue(), date.getDayOfMonth(), suffix);
	}
	private static String CSV(String title, UUID storeId, LocalDate date, String suffix) {
		return CSV(title, storeId.toString(), date, suffix);
	}

	/*
	 * entry point
	 */
	public static void main(String[] args) throws Exception {

		long startTime = System.nanoTime();

		String cwd = System.getProperty("user.dir");
		LOGGER.info("Phenix Challenge, cwd=" + cwd);

		//configuration
		final LocalDate date = LocalDate.of(2017, 05, 14);
		final String dataInputsFolder = "phenix-challenge/data/input";
		final String dataOutputsFolder = "phenix-challenge/data/output";
		final int topLimit = 100;

		//dependencies 'injection'
		final ProductsJournalBuilder journalsLoader = new ProductsJournalBuilderCsv(dataInputsFolder);
		final ProductsGroupSaver statsSaver = new ProductsGroupSaverCsv(dataOutputsFolder);


		//
		// A. tasks for computing the input products information
		//
		// use 2 threads - plain, naive impl but is better than single-threaded
		//
		final ProductsJournalsRepository repository = new ProductsJournalsRepository();

		Runnable rA1 = () ->
		{
			for (int i = 0; i <= 3; ++i) {
				LocalDate dt = date.minusDays(i);
				ProductsJournal dailyProducts = journalsLoader.loadProducts(dt); //might throw
				repository.addJournal(dt, dailyProducts); //all products for all stores (at that date)
			}
		};

		Runnable rA2 = () ->
		{
			for (int i = 4; i <= 6; ++i) {
				LocalDate dt = date.minusDays(i);
				ProductsJournal dailyProducts = journalsLoader.loadProducts(dt); //might throw
				repository.addJournal(dt, dailyProducts); //all products for all stores (at that date)
			}
		};
		LOGGER.info("Loading journals");
		Thread tA1 = new Thread(rA1);
		Thread tA2 = new Thread(rA2);
		tA1.start();
		tA2.start();
		tA1.join();
		tA2.join();
		LOGGER.info("Loading journals done");

		//
		// B. tasks for computing the output statistics
		//
		// use 2 threads - plain, naive impl but is better than single-threaded
		//
		Runnable rB1 = () ->
		{
			{
				//daily - sales volume
				for (UUID storeId : repository.getJournal(date).getStores()) {
					ProductsStat stat = new TopDailyProductsBySalesVolumePerStore(storeId, date, topLimit, repository);  //for each store
					ProductsGroup results = stat.getStatistics();
					statsSaver.saveProducts(results, CSV("top_100_ventes", storeId, date, ""));
				}

				ProductsStat stat = new TopDailyProductsBySalesVolumeGlobally(date, topLimit, repository);
				ProductsGroup results = stat.getStatistics();
				statsSaver.saveProducts(results, CSV("top_100_ventes", "GLOBAL", date, ""));
			}
			{
				//weekly - sales volume
				for(UUID storeId : repository.getJournal(date).getStores()) {
					ProductsStat stat = new TopWeeklyProductsBySalesVolumePerStore(storeId, date, topLimit, repository);  //for each store
					ProductsGroup results = stat.getStatistics();
					statsSaver.saveProducts(results, CSV("top_100_ventes", "GLOBAL", date, "-J7"));
				}

				ProductsStat stat = new TopWeeklyProductsBySalesVolumeGlobally(date, topLimit, repository);
				ProductsGroup results = stat.getStatistics();
				statsSaver.saveProducts(results, CSV("top_100_ventes", "GLOBAL", date,"-J7"));
			}
		};

		Runnable rB2 = () ->
		{
			{
				//daily - sales value
				for (UUID storeId : repository.getJournal(date).getStores()) {
					ProductsStat stat = new TopDailyProductsBySalesValuePerStore(storeId, date, topLimit, repository);  //for each store
					ProductsGroup results = stat.getStatistics();
					statsSaver.saveProducts(results, CSV("top_100_ca", storeId, date, ""));
				}

				ProductsStat stat = new TopDailyProductsBySalesValueGlobally(date, topLimit, repository);
				ProductsGroup results = stat.getStatistics();
				statsSaver.saveProducts(results, CSV("top_100_ca", "GLOBAL", date, ""));
			}
			{
				//weekly - sales value
				for (UUID storeId : repository.getJournal(date).getStores()) {
					ProductsStat stat = new TopWeeklyProductsBySalesValuePerStore(storeId, date, topLimit, repository); //for each store
					ProductsGroup results = stat.getStatistics();
					statsSaver.saveProducts(results, CSV("top_100_ca", storeId, date, "-J7"));
				}
				ProductsStat stat = new TopWeeklyProductsBySalesValueGlobally(date, topLimit, repository);
				ProductsGroup results = stat.getStatistics();
				statsSaver.saveProducts(results, CSV("top_100_ca", "GLOBAL", date, "-J7"));
			}
		};
		LOGGER.info("Creating statistics");
		Thread tB1 = new Thread(rB1);
		Thread tB2 = new Thread(rB2);
		tB1.start();
		tB2.start();
		tB1.join();
		tB2.join();
		LOGGER.info("Creating statistics done");

		long endTime = System.nanoTime();
		LOGGER.info("Total execution time: " + (endTime-startTime)/1000000 + " millis");

		long memoryUsedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		LOGGER.info("Memory used: " + memoryUsedBytes + " bytes");
	}
}