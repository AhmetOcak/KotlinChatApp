# 📱Chat App

<!-- 
#### [TR]
<li>Hesabınız var ise direk giriş yapabilirsiniz. Eğer hesabınız yok is e-posta'nız ile hesap açabilirsiniz.</li>
<li>Hesabınıza sürekli giriş yapmak istemiyorsanız "beni hatırla" kutusunu işaretliyebilirsiniz.</li>
<li>Hesabınızdan çıkış yapabilirsiniz.</li>
<li>Arkadaş eklemek için, eklemek istediğiniz kullanıcıya e-mail'i aracılığıyla arkadaşlık isteği gönderebilirsiniz.</li>
<li>Arkadaşlık isteklerinizi görebilir, istekleri kabul edebilir veya red edebilirsiniz.</li>
<li>Hesabınızı silebilirsiniz. Hesabınızın kullanıcı ismini ve kullanıcı resmini güncelleyebilirsiniz.</li>
<li>Arkadaşlarınızla gerçek zamanlı mesajlaşabilirsiniz.</li>
<li>Arkadaş listenizin bulunduğu ekranda filtreleme yaparak istediğiniz arkadaşınızı hemen bulabilirsiniz.</li>
-->


<!-- #### [EN] -->

## Features
<li>If you have an account, you can log in directly. If you do not have an account, you can open an account with your e-mail.</li>
<li>If you don't want to log into your account all the time, you can tick the "remember me" box.</li>
<li>You can log out of your account.</li>
<li>To add a friend, you can send a friend request to the user you want to add via e-mail.</li>
<li>You can view, accept or decline friend requests.</li>
<li>You can delete your account. You can update the username and user picture of your account.</li>
<li>You can chat with your friends in real time.</li>
<li>You can immediately find the friend you want by filtering on the screen where your friend list is located.</li>


## Libraries📚

[<li>Navigation</li>](https://developer.android.com/guide/navigation)

[<li>Retrofit</li>](https://square.github.io/retrofit)

[<li>Location</li>](https://developer.android.com/training/location)

[<li>ViewModel</li>](https://developer.android.com/topic/libraries/architecture/viewmodel)

[<li>Hilt</li>](https://developer.android.com/training/dependency-injection/hilt-android)

[<li>Room</li>](https://developer.android.com/jetpack/androidx/releases/room)

[<li>Coroutines</li>](https://developer.android.com/kotlin/coroutines)

[<li>Firebase Auth</li>](https://firebase.google.com/docs/auth)

[<li>Firebase Firestore</li>](https://firebase.google.com/docs/firestore)

[<li>Firebase Storage</li>](https://firebase.google.com/docs/storage)

[<li>Gson</li>](https://github.com/google/gson)

## Modules

#### App Module
Initializes the project. It contains the main activity, Application class(created for hilt) and google-service.json file(it's coming from firebase).

#### Domain Module
It contains the interfaces and models.

#### Data Module
This module includes local and network operations. It takes the data and makes it ready. It contains the entity models, repositories, mappers, usecases.

#### Core Module
It contains base classes.

#### Features Module
This module includes ui operations. It shows data to user.

## Modularization Graph

<p align="left" width="100%">
  <img src="https://user-images.githubusercontent.com/73544434/184920127-30958647-72c7-4304-907f-ac13ca9da99e.jpg"/>
</p>

* ``:features`` module depends on core and data modules

* ``:app`` module depends on core and features modules

* ``:data`` module depends on domain module


## Images from app

| Login            | <img src="https://user-images.githubusercontent.com/73544434/184932976-8cdff7eb-1241-4e6d-86b7-54ab3a33780b.png" width="360" height="640"/>  | Delete Account  | <img src="https://user-images.githubusercontent.com/73544434/184935558-239f7f57-4e84-482d-a6cd-9d42580e21c9.png" width="360" height="640"/>  |
|------------------|---|---|---|
| Register         | <img src="https://user-images.githubusercontent.com/73544434/184933459-edd9033c-969b-4b28-9e5f-06da8cd4f425.png" width="360" height="640"/>  | Edit Profile  | <img src="https://user-images.githubusercontent.com/73544434/184935673-2b70fb61-918a-45e5-8ba8-3fd3e9770824.png" width="360" height="640"/>  |
| Message          | <img src="https://user-images.githubusercontent.com/73544434/184935287-74f3fecb-2c33-456a-9157-967d2e8f3e5a.png" width="360" height="640"/>  | Friend Request  | <img src="https://user-images.githubusercontent.com/73544434/184935787-379ebe6a-b103-464b-8abb-0e5ab4a4f8a5.png" width="360" height="640"/>  |
| Account Settings | <img src="https://user-images.githubusercontent.com/73544434/184936691-711ed9e4-2eb0-4d32-88eb-f2079e30bdf0.png" width="360" height="600"/>  | Add Friend  | <img src="https://user-images.githubusercontent.com/73544434/184936065-47e1212f-72a0-400f-a248-428f522cd6ab.png" width="360" height="640"/>  |
| Log Out          | <img src="https://user-images.githubusercontent.com/73544434/184935439-c297f992-bf03-4994-b52c-65763df02aa6.png" width="360" height="640"/>  | Chat  | <img src="https://user-images.githubusercontent.com/73544434/184936303-087ff69a-e33c-4c1d-96d1-a1e50f3e3b90.png" width="360" height="640"/>  |


## Firebase setup

### Create project

<p align="left" width="100%">
  <img src="https://user-images.githubusercontent.com/73544434/185697776-19dae250-92bf-425b-8134-29be8f7f2397.png" height="500"/>
</p>

<p align="left" width="100%">
  <img src="https://user-images.githubusercontent.com/73544434/185698206-eba3fd34-ec08-4039-b06a-90eb4f92126d.png" height="500"/>
</p>

### Create Android project

<p align="left" width="100%">
  <img src="https://user-images.githubusercontent.com/73544434/185698486-4eda01f3-3357-4de7-8417-13cb1c99d7fe.jpg" height="500"/>
</p>

Download the google-services.json and copy to ``app`` directory
<p align="left" width="100%">
  <img src="https://user-images.githubusercontent.com/73544434/185698508-6a0f04ad-af47-4228-8d5c-fdf8244b16e9.png" height="500"/>
</p>

Create database
<p align="left" width="100%">
  <img src="https://user-images.githubusercontent.com/73544434/185698787-1ff63648-28d2-4f88-8945-ef98ca64b72f.jpg" height="500"/>
</p>
