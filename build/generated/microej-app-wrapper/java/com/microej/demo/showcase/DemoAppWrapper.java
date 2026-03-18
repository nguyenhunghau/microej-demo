package com.microej.demo.showcase;
import ej.kf.FeatureEntryPoint;
public class DemoAppWrapper implements FeatureEntryPoint {
    @Override
    public void start() {
        com.microej.demo.showcase.DemoApp.main(new String[0]);
    }
    @Override
    public void stop() {
        // do nothing, the stop method is not needed since this class is the 
        // wrapper class of the Application main class.
    }
}
