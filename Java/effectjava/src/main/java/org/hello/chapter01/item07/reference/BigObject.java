package org.hello.chapter01.item07.reference;

import java.util.List;

public class BigObject {

    private List<Object> resources;

    public BigObject() {}

    public BigObject(List<Object> resources) {
        this.resources = resources;
    }

    public static class ResourceCleaner implements Runnable {

        private List<Object> resourceToClean;

        public ResourceCleaner(List<Object> resourceToClean) {
            this.resourceToClean = resourceToClean;
        }

        @Override
        public void run() {
            resourceToClean = null;
            System.out.println("Cleaned");
        }
    }
}
