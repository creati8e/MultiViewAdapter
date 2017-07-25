# MultiViewAdapter

Library for easy displaying mutliple view types in RecyclerView adapter.
You even no need to write custom adapter and ViewHolder.

# Setup
Latest version is **1.0**

Firstly add repository to your project's build.gradle 

```groovy
allprojects {
    repositories {
        ...
        maven { url "http://dl.bintray.com/creati8e/maven" }
    }
}
```

Add dependency to your app's build.gradle 

```groovy
dependencies {
    compile 'serg.chuprin:multiviewadapter:1.0'
}
```

# How to

* Write renderer

```kotlin
class UserRenderer : ViewRenderer<UserEntity, ViewHolder>() {

    override fun type(): Int = R.layout.list_item_user

    override fun isViewForType(model: Any): Boolean = model is UserEntity

    override fun bindView(holder: ViewHolder, model: UserEntity) {
        holder.itemView.textView.text = model.login
    }
}
```

* Register renderer

```kotlin
class MainActivity : AppCompatActivity() {

    private var adapter = MultiViewAdapter()
    ...
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        adapter.registerRenderer(UserRenderer())
    }
}
```
Done!

# Click listeners

Long and single click listeners are available.
To add click llstener, override method **onVhCreated** in your renderer.
Then set listener on view what you want.

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





