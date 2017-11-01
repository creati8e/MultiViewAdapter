# MultiViewAdapter

Library for easily displaying mutliple view types in RecyclerView adapter.
You don't even need to write custom adapter and ViewHolder.

# Setup
Latest version is **1.1/0**

Add dependency to your app's build.gradle 

```groovy
dependencies {
    compile 'serg.chuprin:multiviewadapter:1.1.0'
}
```

# How to

* Write renderer

```kotlin
class UserRenderer : SimpleViewRenderer<UserEntity>() {

    override val type: Int = R.layout.list_item_user

    override fun bindView(holder: ViewHolder, model: UserEntity) {
        holder.itemView.textView.text = model.login
    }
}
```

* Register renderer

```kotlin
class MainActivity : AppCompatActivity() {

    private val adapter = MultiViewAdapter().apply{
        registerRenderer(UserRenderer())   
    }
}
```
Done!

# Click listeners

To add click listener, override method **onVhCreated** in your renderer.
Then set listener on the view.

```kotlin
class UserRenderer : ViewRenderer<UserEntity, ViewHolder>() {

    ...
    override fun onVhCreated(holder: ViewHolder, clickListener: Click?,
                             longClickListener: LongClick?) {
        holder.itemView.userLogo.setOnClickListener {
            clickListener?.onClick(it, holder.layoutPosition)
        }
    }
}

```






