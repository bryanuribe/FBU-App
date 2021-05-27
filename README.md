# PickUp+

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

**Figma**
https://www.figma.com/file/Cus8DUS9m3aUvlcP9hx0Ne/FBU-App?node-id=0%3A1

## Overview
### Description
Allows users to connect and find other local users that want to participate in a pick up game. The home screen will have all upcoming events nearby. 

Users can also start their own private or public game. By starting a private event, only users within your organization will be able to see your event. By starting a public event, it will appear for all users. 

Each event will feature details of the location/time and any notes that should be considered.


### App Evaluation

**Category:** Allows users to connect and find other local users that want to participate in a pick up game. The home screen will have all upcoming events nearby.  Users can also start their own private or public game. By starting a private event, only users within your organization will be able to see your event. By starting a public event, it will appear for all users. Each event will feature details of the location/time and any notes that should be considered.

**Story:** In downtime, this app will be useful to finding other users who want to play a pick up game locally. Especially useful if your friends are busy at the moment.

**Market:** Any soccer or basketball player.

**Habit:** Before going out to play a game with friends or by yourself, this app will be useful to find other local users who want to participate in a larger game.

**Scope:** V1 will allow you to view and create public / private events. Public events are visible to all people who are using the app. Private events are limited to user created organizations. Each event specifies the location, time, and other important notes to consider. To view these events the user will create a unique profile. You are able to edit your profile to display basic information such as a profile picture, username, bio, and the current team you are playing for. The user is also able to create organizations, where they can add their friends, and other people they meet casually so they can view their events.

V2 will allow you to further customize your event, and it will allow users to RSVP for an event. It will also display the list of users able to attend the event publicly. 

V3 will allow you to chat with users within your organization. 

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* App has multiple views
    
* App interacts with a database (e.g. Parse)

* You can log in/log out of your app as a user

* You can sign up with a new user profile

* Somewhere in your app you can use the camera to take a picture and do something with the picture (e.g. take a photo and share it to a feed, or take a photo and set a user’s profile picture)

* Your app integrates with a SDK (e.g. Google Maps SDK, Facebook SDK)

* Your app contains at least one more complex algorithm (talk over this with your manager)

* Your app uses gesture recognizers (e.g. double tap to like, e.g. pinch to scale)

* Your app use an animation (doesn’t have to be fancy) (e.g. fade in/out, e.g. animating a view growing and shrinking)

* Your app incorporates an external library to add visual polish

**Optional Nice-to-have Stories**

* Allow further customization of events (allow user to specify skill level)
* Allow users to RSVP for an event, and show all users who have RSVP’d
* Add user chat in organizations


### 2. Screen Archetypes

* Login Screen
   * User can login
* Registration Screen
   * User can create a new account
* Stream
   * User can view a google maps feed of event locations
   * User can tap a photo to view detail page of the event 
   * User can swipe a detail to indicate they are attending
* Creation
   * User can post a new event to the google maps feed
* Search
   * User can search for events
   * User can follow/unfollow organizations


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Stream Screen
* Creation screen
* Profile screen
* User events screen

**Flow Navigation** (Screen to Screen)

* Login Screen
   * Stream Screen
* Registration Screen
   * Stream Screen
* Stream Screen (Google Maps)
   * Creation Screen
* Creation Screen
   * Home (after you finish posting the photo)
   * In the actual wireframe, you will need multiple screens to represent the creation process to add filters, etc.
* Search Screen


## Wireframes
**Figma**
https://www.figma.com/file/Cus8DUS9m3aUvlcP9hx0Ne/FBU-App?node-id=0%3A1


