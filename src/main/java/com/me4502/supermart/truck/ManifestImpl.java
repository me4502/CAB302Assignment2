package com.me4502.supermart.truck;

import com.google.common.collect.ImmutableSet;

/**
 * Implementation for {@link Manifest}.
 * {@inheritDoc}
 *
 * @author Madeline Miller
 */
public class ManifestImpl implements Manifest {

    private ImmutableSet<Truck> trucks;

    private ManifestImpl(ImmutableSet<Truck> trucks) {
        this.trucks = trucks;
    }

    @Override
    public ImmutableSet<Truck> getTrucks() {
        return this.trucks;
    }

    /**
     * {@inheritDoc}
     *
     * @author Madeline Miller
     */
    public static class ManifestBuilderImpl implements Manifest.Builder {

        private ImmutableSet.Builder<Truck> trucksBuilder = new ImmutableSet.Builder<>();

        @Override
        public Builder addTruck(Truck truck) {
            trucksBuilder.add(truck);
            return this;
        }

        @Override
        public Manifest build() {
            return new ManifestImpl(trucksBuilder.build());
        }

        @Override
        public Builder reset() {
            this.trucksBuilder = new ImmutableSet.Builder<>();
            return this;
        }
    }
}
