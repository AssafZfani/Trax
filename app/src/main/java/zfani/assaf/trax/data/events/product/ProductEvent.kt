package zfani.assaf.trax.data.events.product

sealed class ProductEvent {

    object EmptyProductName : ProductEvent()

    object EmptyProductCategory : ProductEvent()

    object EmptyProductImage : ProductEvent()

    object ProductInserted : ProductEvent()

    object ProductUpdated : ProductEvent()

    object ProductDeleted : ProductEvent()
}
