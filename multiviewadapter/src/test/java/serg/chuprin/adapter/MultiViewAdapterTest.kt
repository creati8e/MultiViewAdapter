package serg.chuprin.adapter

import org.junit.Assert
import org.junit.Test

class MultiViewAdapterTest {

    private val adapter = MultiViewAdapter()

    @Test
    fun registerRenderer1() {

        val testRenderer = object : SimpleViewRenderer<Any>() {
            override val type: Int = -1
        }

        Assert.assertTrue(adapter.renderers.size() == 0)

        adapter.registerRenderer(testRenderer)

        Assert.assertTrue(adapter.renderers.size() == 1)

    }

    @Test
    fun registerRenderer2() {

        val testRenderer = object : SimpleViewRenderer<Any>() {
            override val type: Int = -1
        }

        Assert.assertTrue(adapter.renderers.size() == 0)

        adapter.registerRenderer(testRenderer, Any::class.java)

        Assert.assertTrue(adapter.renderers.size() == 1)

    }

    @Test
    fun removeRenderer() {
        val testRenderer = object : SimpleViewRenderer<Any>() {
            override val type: Int = -1
        }

        Assert.assertTrue(adapter.renderers.size() == 0)

        adapter.registerRenderer(testRenderer, Any::class.java)

        Assert.assertTrue(adapter.renderers.size() == 1)

        adapter.removeRenderer(Any::class.java)

        Assert.assertTrue(adapter.renderers.size() == 0)
    }

    @Test
    fun removeRenderer2() {
        val testRenderer = object : SimpleViewRenderer<Any>() {
            override val type: Int = -1
        }

        Assert.assertTrue(adapter.renderers.size() == 0)

        adapter.registerRenderer(testRenderer)

        Assert.assertTrue(adapter.renderers.size() == 1)

        adapter.removeRenderer(Any::class.java)

        Assert.assertTrue(adapter.renderers.size() == 0)
    }

    @Test
    fun removeRenderer3() {
        val testRenderer = object : SimpleViewRenderer<Any>() {
            override val type: Int = -1
        }

        Assert.assertTrue(adapter.renderers.size() == 0)

        adapter.registerRenderer(testRenderer)

        Assert.assertTrue(adapter.renderers.size() == 1)

        adapter.removeRenderer(testRenderer)

        Assert.assertTrue(adapter.renderers.size() == 0)
        Assert.assertTrue(adapter.rendererTypes.isEmpty())
    }
}