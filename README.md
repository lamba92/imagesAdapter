[![](https://jitpack.io/v/Lamba92/imagesAdapter.svg)](https://jitpack.io/#Lamba92/imagesAdapter)

# ImagesAdapter

A `RecyclerView.Adapter` that allows to add, remove, blur and slideshow images inside a `RecyclerView`.

This is how it works with `allowDelete = false`   |   This is with `allowDelete = false`


![alt text](https://raw.githubusercontent.com/lamba92/imagesAdapter/master/stuff/1.gif)![alt text](https://raw.githubusercontent.com/lamba92/imagesAdapter/master/stuff/2.gif)

### Installing

Add the [JitPack.io](http://jitpack.io) repository to the project `build.grade`:
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Then import the latest version in the `build.gradle` of the modules you need:

```
dependencies {
    implementation 'com.github.Lamba92:imagesAdapter:{latest_version}'
}
```

## Getting Started

Create a new `ImagesAdapter` and assign it to a `RecyclerView`:
```
val imagesAdapter = ImagesAdapter(context) //by default allowDelete = false
recyclerView.adapter = imagesAdapter
```

Then add all the image files:
```
imagesAdapter.add(file)  //single file, by default isImageNew = true
imagesAdapter.add(files, false) //a List of files
```

Keep in mind that `isImageNew` flag changes the behaviour of the delete fab. If set to true, tapping the fab will remove the image from the adapter completely. If set to `false` tapping the fab will blur the image and the image won't be present when using `getImages()`

To retrieve the images inside the adapter use `getImages(type: ImagesType)`
```
imagesAdapter.getImages(ImagesAdapter.ImagesType.NewImages) //by default type = ImagesType.NewImages
imagesAdapter.getImages(ImagesAdapter.ImagesType.OldImages).apply{
    //do something with the images List
}
```

## Authors

* **Lamberto Basti**  - [lamba92](https://github.com/lamba92)