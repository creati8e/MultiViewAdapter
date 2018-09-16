package serg.chuprin.adapter

import org.junit.Assert
import org.junit.Test

class MultiViewAdapterTest {

    private val adapter = MultiViewAdapter()
    private val testRenderer = object : SimpleViewRenderer<Any>() {
        override val type: Int = -1
    }

    @Test
    fun `renderer should be added after registering via reified function`() {
        adapter.registerRenderer(testRenderer)
        assertRenderersRegistered(1)
    }

    @Test
    fun `renderer should be added after registering via class specifying`() {
        adapter.registerRenderer(testRenderer, Any::class.java)
        assertRenderersRegistered(1)
    }

    @Test
    fun `renderer should be removed after registering via class specifying and removing via class`() {
        adapter.registerRenderer(testRenderer, Any::class.java)

        assertRenderersRegistered(1)

        adapter.removeRenderer(Any::class.java)

        assertRenderersRegistered(0)
    }

    @Test
    fun `renderer should be removed after registering via reified function and removing via class`() {
        adapter.registerRenderer(testRenderer)

        assertRenderersRegistered(1)

        adapter.removeRenderer(Any::class.java)

        assertRenderersRegistered(0)
    }

    @Test
    fun `renderer should be removed after instance registering and removing`() {
        adapter.registerRenderer(testRenderer)

        assertRenderersRegistered(1)

        adapter.removeRenderer(testRenderer)

        assertRenderersRegistered(0)
    }

    private fun assertRenderersRegistered(count: Int) {
        Assert.assertEquals(count, adapter.renderers.size())
        Assert.assertEquals(count, adapter.rendererTypes.size)
    }
}