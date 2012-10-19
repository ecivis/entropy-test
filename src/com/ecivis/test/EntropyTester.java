package com.ecivis.test;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Set;

public class EntropyTester {

	private static int defaultSampleCount = 100;

	public static void main(String[] args) {
		String action = "";
		String provider = "";
		String algorithm = "";
		int sampleCount = defaultSampleCount;
		
		if (args.length > 0) {
			action = args[0];
		}

		if (action.equals("list")) {
			listProvidersAndAlgorithms();
		}
		
		if (action.equals("test")) {
			if (args.length == 2 || args.length == 4) {
				// Attempt to parse the second argument as the sample count
				try {
					sampleCount = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					System.out.println("Error: Sample count provided is not an integer.");
					System.exit(1);
				}
			}
			
			if (args.length == 3 || args.length == 4) {
				// Use the last two arguments as provider and algorithm
				provider = args[args.length - 2];
				algorithm = args[args.length - 1];
			}
			runTest(sampleCount, provider, algorithm);
		}
		
		// Unsupported action
		showUsage();
	}

	public static void runTest(int sampleCount, String provider, String algorithm) {
		SecureRandom secureRandom = null;

		long startTime = System.currentTimeMillis();
		if (provider.length() > 0 && algorithm.length() > 0) {
			try {
				secureRandom = SecureRandom.getInstance(algorithm, provider);
				if (secureRandom == null) {
					System.out.println("Error: Could not instantiate SecureRandom from " + provider + " " + algorithm);
					System.exit(1);
				}
			} catch (NoSuchAlgorithmException e) {
				System.out.println("Error: No such algorithm " + algorithm);
				System.exit(1);
			} catch (NoSuchProviderException e) {
				System.out.println("Error: No such provider " + provider);
				System.exit(1);
			}
		} else {
			secureRandom = new SecureRandom();
		}
		long taskTime = System.currentTimeMillis() - startTime; 
		System.out.println("Instantiated " + secureRandom.getProvider() + " " + secureRandom.getAlgorithm() + " SecureRandom instance in " + taskTime + " ms");
		
		System.out.println("Requesting " + sampleCount + " samples, 16 bytes each. Each of the following dots represents 1 Kb");

		byte[] bucket = new byte[16];
		startTime = System.currentTimeMillis();
		for (int i = 0; i < sampleCount; i++) {
			secureRandom.nextBytes(bucket);
			if (i % 1024 == 0) {
				System.out.print(".");
			}
		}
		taskTime = System.currentTimeMillis() - startTime;
		System.out.println(".");
		System.out.println("Received " + sampleCount * 16  + " bytes of random data in " + taskTime + " ms");
		System.exit(0);
	}
	
	public static void listProvidersAndAlgorithms() {
		System.out.println("SecureRandom Implementations Available:");
		Provider[] providers = Security.getProviders(); 
		
		for (Provider provider : providers) {
			Set<Provider.Service> services = provider.getServices();
			for (Provider.Service service : services) {
				if (service.getType().equals("SecureRandom")) {
					System.out.print("\t" + service.toString());
				}
			}
		}
		System.exit(0);
	}
	
	public static void showUsage() {
		System.out.println("Usage: action [options]");
		System.out.println("Specify an action and provide required options:");
		System.out.println("  list - Shows the available security providers and algorithms for SecureRandom implementations.");
		System.out.println("  test - Run the entropy test. Optional arguments are sampleCount, provider, and algorithm");
		System.out.println("");
		System.out.println("Examples:");
		System.out.println("  java -jar ecivis-entropy-test.jar list");
		System.out.println("  java -jar ecivis-entropy-test.jar test 25");
		System.out.println("  java -jar ecivis-entropy-test.jar test SUN NativePRNG");
		System.out.println("  java -jar ecivis-entropy-test.jar test 1000 SUN SHA1PRNG");
		System.exit(1);
	}
	
}
