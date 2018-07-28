# MultiViewAdapter

Library for easily displaying mutliple view types in RecyclerView adapter.
You don't even need to write custom adapter and ViewHolder.

# Setup
Latest version is **1.2.4**

Add dependency to your app's build.gradle 

```groovy
dependencies {
    implementation 'serg.chuprin:multiviewadapter:1.2.4'
}
```

Also you can use adapter with kotlin android-extensions

```groovy
dependencies {
    implementation 'serg.chuprin:multiviewadapter-kt-extensions:1.2.4'
}
```

# How to

* Write renderer

If you want to get power of kotlin android-extensions and don't want to write 
 ViewHolder, use **ContainerRenderer**.

Or you can use library in old way and use **ViewRenderer** and write custom
ViewHolder

```kotlin
class UserRenderer : ContainerRenderer<UserEntity>() {

    override val type: Int = R.layout.list_item_user

    override fun bindView(holder: ContainerHolder, model: UserEntity) {
        holder.itemView.textView.text = model.login
    }
}
```

* Register renderer

```kotlin
val adapter = MultiViewAdapter().apply{
    registerRenderer(UserRenderer())   
}
```

If you use Java, you need to specify type explicitly

```kotlin
final MultiViewAdapter adapter = new MultiViewAdapter();
adapter.registerRenderer(new UserRenderer(), UserEntity.class);
```
Done!

# Click listeners

To add click listener, override method **onVhCreated** in your renderer.
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