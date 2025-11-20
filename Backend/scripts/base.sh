#!/bin/sh

#### Configuration

JAVA=java
MASTER=gr.opa.booking.master.App
WORKER=gr.opa.booking.worker.App
REDUCER=gr.opa.booking.reducer.App
CLIENT=gr.opa.booking.client.App

#### Utility

run_component() {
  eval "$JAVA -cp target/booking-system-1.0-SNAPSHOT-jar-with-dependencies.jar $1"
}

