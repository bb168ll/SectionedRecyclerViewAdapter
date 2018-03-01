package io.github.luizgrp.sectionedrecyclerviewadapter2.sectionedrecyclerviewadapter;

/**
 * Abstract {@link Section} with no states.
 */
public abstract class StatelessSection<T> extends Section<T> {

    /**
     * Create a stateless Section object based on {@link SectionParameters}
     * @param sectionParameters section parameters
     */
    public StatelessSection(SectionParameters sectionParameters) {
        super(sectionParameters);

        if (sectionParameters.loadingResourceId != null) {
            throw new IllegalArgumentException("Stateless section shouldn't have a loading state resource");
        }

        if (sectionParameters.failedResourceId != null) {
            throw new IllegalArgumentException("Stateless section shouldn't have a failed state resource");
        }

        if (sectionParameters.emptyResourceId != null) {
            throw new IllegalArgumentException("Stateless section shouldn't have an empty state resource");
        }
    }

    @Override
    public final void onBindLoadingViewHolder(ViewHolder holder) {
        super.onBindLoadingViewHolder(holder);
    }

//    @Override
//    public final ViewHolder getLoadingViewHolder(View view) {
//        return super.getLoadingViewHolder(view);
//    }

    @Override
    public final void onBindFailedViewHolder(ViewHolder holder) {
        super.onBindFailedViewHolder(holder);
    }

//    @Override
//    public final ViewHolder getFailedViewHolder(View view) {
//        return super.getFailedViewHolder(view);
//    }

    @Override
    public final void onBindEmptyViewHolder(ViewHolder holder) {
        super.onBindEmptyViewHolder(holder);
    }

//    @Override
//    public final ViewHolder getEmptyViewHolder(View view) {
//        return super.getEmptyViewHolder(view);
//    }
}
