## Entropy Test

This is a command line Java application that will enumerate the available security providers and algorithms used with a SecureRandom implementation. It will test the specified SecureRandom implementation by requesting a specified number of samples of 16 bytes of entropy. The test will ouput the time take to instantiate the entropy source and the time taken to perform sampling.

## Building

No additional libraries are required while compiling. This was created using Java 6, and probably works with Java 7. Run the Ant build file specifying the jar target:

```ant jar```

The built file will appear in the dist directory.

## Usage

    Specify an action and provide required options:
        list - Shows the available security providers and algorithms for SecureRandom implementations.
        test - Run the entropy test. Optional arguments are sampleCount, provider, and algorithm

    Examples:
        java -jar ecivis-entropy-test-X.YY.jar list
        java -jar ecivis-entropy-test-X.YY.jar test 25
        java -jar ecivis-entropy-test-X.YY.jar test SUN NativePRNG
        java -jar ecivis-entropy-test-X.YY.jar test 1000 SUN SHA1PRNG

## Sample Output

    $ java -jar dist/ecivis-entropy-test-0.04.jar test 10000 SUN NativePRNG
    Instantiated SUN version 1.6 NativePRNG SecureRandom instance in 86 ms
    Requesting 10000 samples, 16 bytes each. Each of the following dots represents 1 Kb
    ...........
    Received 160000 bytes of random data in 89 ms

## License

Copyright 2012 eCivis, Inc.

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
