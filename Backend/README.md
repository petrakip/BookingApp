# Notes

## How to build

```bash
mvn clean compile assembly:single
```

Please note that this creates a single JAR file that contains the applications TOGETHER with their 
dependencies. This was done for simplifying running the applications, and at the same time not have to 
create different Maven projects for each one.

Note that you can also use the following command to build:

```bash
./scripts/build.sh
```

## How to run manually

```bash
java -cp <JAR file> <class file>
```

For example, if our JAR resides in `target/`, then we should use the following command to run the master:

```bash
java -cp \
  target/booking-system-1.0-SNAPSHOT-jar-with-dependencies.jar \
  gr.opa.booking.master.WorkerApplication
```

## Scripts

There are various scripts located in the folder with the same name.

1. `base.sh`: This is the "library" used by other scripts. Don't run it by itself.
2. `build.sh`: This script can be used to build the project.
3. `run_client.sh`: This executes the client.
4. `run_worker.sh`: This executes the worker.
5. `run_reducer.sh`: This executes the reducer.
6. `run_master.sh`: This executes the master.

Be sure to grant execution permissions to the scripts. Run the following from a bash shell:

```bash 
chmod +x scripts/*
```

## Running the whole stack in the proper order

The order in which the applications are to be started is the following:

1. Master
2. Reducer (needs to connect to Master on startup)
3. Worker (connects to both the Master and Reducer on startup)
4. Client (as needed)