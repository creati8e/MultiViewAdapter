package serg.chuprin.adapter

import androidx.collection.SparseArrayCompat

@Suppress("UNCHECKED_CAST")
class RendererDelegate<T : Any> {

    val renderers = SparseArrayCompat<ViewRenderer<T, ViewHolder>>()
    val rendererTypes = mutableMapOf<Class<*>, ViewRenderer<T, ViewHolder>>()

    inline fun <reified R : Any> registerRenderer(renderer: ViewRenderer<R, *>) {
        if (renderers.get(renderer.type) != null) {
            throw IllegalStateException("ViewRenderer already exist with this type: " + renderer.type)
        }
        renderers.put(renderer.type, renderer as ViewRenderer<Any, ViewHolder>)
        rendererTypes[R::class.java] = renderer
    }

    fun <R : Any> registerRenderer(renderer: ViewRenderer<R, *>, clazz: Class<R>) {
        if (renderers.get(renderer.type) != null) {
            throw IllegalStateException("ViewRenderer already exist with this type: " + renderer.type)
        }
        renderers.put(renderer.type, renderer as ViewRenderer<Any, ViewHolder>)
        rendererTypes[clazz] = renderer
    }

    fun removeRenderer(clazz: Class<*>): Boolean {
        val removedRenderer = rendererTypes.remove(clazz) ?: return false
        val index = renderers.indexOfValue(removedRenderer).takeIf { it != -1 } ?: return false

        renderers.removeAt(index)
        return true
    }

    fun removeRenderer(renderer: ViewRenderer<R, *>): Boolean {
        var classType: Class<*>? = null

        for ((registeredType, registeredRenderer) in rendererTypes) {
            if (registeredRenderer.type == renderer.type) {
                classType = registeredType
                break
            }
        }
        if (classType != null) rendererTypes.remove(classType)
        val index = renderers
            .indexOfValue(renderer as ViewRenderer<Any, ViewHolder>)
            .takeIf { it != -1 }
            ?: return false

        renderers.removeAt(index)
        return true
    }

    fun getRendererForClass(itemClass: Class<*>): ViewRenderer<T, ViewHolder> {
        return requireNotNull(rendererTypes[itemClass]) {
            "No renderer registered for type: ${itemClass.simpleName}"
        }
    }

    fun getRendererForViewType(viewType: Int): ViewRenderer<T, ViewHolder> {
        return requireNotNull(renderers.get(viewType)) {
            "No renderer found for view type: $viewType"
        }
    }
}