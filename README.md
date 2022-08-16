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


#### [EN]
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




