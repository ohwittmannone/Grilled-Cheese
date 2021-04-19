# Grilled-Cheese
Do you love grilled cheese as much as me? Well now you can set your phone's wallpaper to a picture of a random grilled cheese or today's hottest post from the Subreddit [/r/grilledcheese!](https://www.reddit.com/r/grilledcheese/) I made this app to get familiar with/show off some libraries.

<img src ="https://user-images.githubusercontent.com/17746085/115168856-53652280-a08a-11eb-8f88-b53cc1a5e4ec.gif" width = 350 />

### Libraries Used
* Retrofit2
* OkHttp3
* Coroutines
* Gson
* Glide

The purpose of this was to get used MVVM using ViewModels and to use Jetpack libs with coroutines. I had initially intended to have it set your wallpaper everyday with the use of a coroutine worker, and although I now know how it works, it turns out it is inadequate for this use case because it's not ideal to make network calls with it. Instead, a forground service could potentially work.
