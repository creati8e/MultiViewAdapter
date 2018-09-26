[ ![Download](https://api.bintray.com/packages/creati8e/maven/multiviewadapter/images/download.svg) ](https://bintray.com/creati8e/maven/multiviewadapter/_latestVersion)
# MultiViewAdapter

Library for easily displaying multiple view types in RecyclerView adapter.
You don't even need to write custom adapter and ViewHolder.

**Note: library is AndroidX-only**

# Setup
Library is available on `jCenter`

Add dependency to your app's `build.gradle`

```groovy
dependencies {
    implementation "serg.chuprin:multiviewadapter:$latestVersion"
}
```

Also you can use adapter with `kotlin-android-extensions`

```groovy
apply plugin: 'kotlin-android-extensions'

androidExtensions {
    experimental = true
}

dependencies {
    implementation "serg.chuprin:multiviewadapter-kt-extensions:$latestVersion"
}
```

# How to use

### Choose which adapter to use
* `MultiViewAdapter` - if you don't have any strict type hierarchy,
 all items will have Any type
* `TypedMultiViewAdapter` - if you have specific type (sealed class, for example)
* `DiffMultiViewAdapter` - if you want to get benefit from automatic
 lists diffing and animations

## Create renderer

Renderer - class that represents concrete view type and can render concrete model

If you want to get power of kotlin android-extensions and don't want
to write ViewHolder, use `ContainerRenderer`.

Or you can use library in old way and create custom `ViewRenderer`

```kotlin
class UserRenderer : ContainerRenderer<UserEntity>() {

    override val type: Int = R.layout.list_item_user

    override fun bindView(holder: ContainerHolder, model: UserEntity) {
        holder.itemView.textView.text = model.login
    }
}
```

Register renderer

```kotlin
val adapter = MultiViewAdapter().apply {
    registerRenderer(UserRenderer())   
}
```

If you use Java, you need to specify type explicitly

```kotlin
MultiViewAdapter adapter = new MultiViewAdapter();
adapter.registerRenderer(new UserRenderer(), UserEntity.class);
```
Done!

# Click listeners

To add click listener, override method `onVhCreated` in your renderer.
Then set listener on the view.

```kotlin
class UserRenderer : ContainerRenderer<UserEntity>() {

    ...
    override fun onVhCreated(
        holder: ContainerHolder,
        clickListener: Click?,
        longClickListener: LongClick?
    ) {
        holder.itemView.userLogo.setOnClickListener {
            clickListener?.onClick(it, holder.layoutPosition)
        }
    }
}

```

Then react to click events:

```kotlin
adapter.clickListener = { model, view, position -> TODO() }
```

# DiffUtil

Library has `DiffMultiViewAdapter` - the equivalent to `ListAdapter`
from support library.
It uses `AsyncListDiffer` internally for calculating diff on background thread.
You don't need to care about any notifying adapter about data changes,
just set new items!