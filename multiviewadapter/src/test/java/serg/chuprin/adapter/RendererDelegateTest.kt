package serg.chuprin.adapter

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RendererDelegateTest {

    private lateinit var rendererDelegate: RendererDelegate<Any>
    private val testRenderer = object : SimpleViewRenderer<Any>() {
        override val type: Int = -1
    }

    @Before
    fun setup() {
        rendererDelegate = RendererDelegate()
    }

    @Test
    fun `renderer should be added after registering via reified function`() {
        rendererDelegate.registerRenderer(testRenderer)
        assertRenderersRegistered(1)
    }

    @Test
    fun `adding same renderer twice will cause exception being thrown`() {
        rendererDelegate.registerRenderer(testRenderer)
        assertRenderersRegistered(1)

        var thrown = false
        try {
            rendererDelegate.registerRenderer(testRenderer)
        } catch (e: IllegalStateException) {
            thrown = true
        }
        Assert.assertTrue(thrown)
        assertRenderersRegistered(1)
    }

    @Test
    fun `renderer should be added after registering via class specifying`() {
        rendererDelegate.registerRenderer(testRenderer, Any::class.java)
        assertRenderersRegistered(1)
    }

    @Test
    fun `renderer should be removed after registering via class specifying and removing via class`() {
        rendererDelegate.registerRenderer(testRenderer, Any::class.java)

        assertRenderersRegistered(1)

        rendererDelegate.removeRenderer(Any::class.java)

        assertRenderersRegistered(0)
    }

    @Test
    fun `renderer should be removed after registering via reified function and removing via class`() {
        rendererDelegate.registerRenderer(testRenderer)

        assertRenderersRegistered(1)

        rendererDelegate.removeRenderer(Any::class.java)

        assertRenderersRegistered(0)
    }

    @Test
    fun `renderer should be removed after instance registering and removing`() {
        rendererDelegate.registerRenderer(testRenderer)

        assertRenderersRegistered(1)

        rendererDelegate.removeRenderer(testRenderer)

        assertRenderersRegistered(0)
    }

    @Test
    fun `removing non-existent renderer should return false`() {
        Assert.assertFalse(rendererDelegate.removeRenderer(testRenderer))
    }

    private fun assertRenderersRegistered(count: Int) {
        Assert.assertEquals(count, rendererDelegate.renderers.size())
        Assert.assertEquals(count, rendererDelegate.rendererTypes.size)
    }
}