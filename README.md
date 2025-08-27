__________________ 1 _____________________
for creating new user, 
no auth require as usual, 
just give to json object with all parameters like following

{
  "userName": "john",
  "password": "12345",
  "fullName": "John Doe",
  "active": 1
}
POST http://localhost:8080/api1/users


__________________ 2 ___________________________________
for retrival all user, only staff can do with login condition
GET http://localhost:8080/api1/users


__________________ 3 _________________\
staff can get eny user individually
but not other user can get other's  user account

GET http://localhost:8080/api1/users/harsh
authentication require

__________________ 4 _________________
for password change, user and staff also update
need to log in with old pass
provide following json object
{
  "userName": "het",
  "password": "333"
//   "fullName": "Het Mishra",
//   "active": 1
}

url will be 
PUT http://localhost:8080/api1/users/pass

__________________ 5 _________________
for full name change, all like pass change

{
  "userName": "john",
//   "password": "333"
  "fullName": "john pandya"
//   "active": 1
}

PUT http://localhost:8080/api1/users/fullName

__________________ 6 _________________
for delete, staff can do delete any account , but user only its own account
<img width="891" height="607" alt="image" src="https://github.com/user-attachments/assets/3b53108a-8f32-4309-a7ff-af85ce48636a" />


on succesful delete
<img width="929" height="781" alt="image" src="https://github.com/user-attachments/assets/c44c9ad4-13d2-48f0-b424-fd8672708908" />

__________________ tweets _________________
__________________ 1 ___________________________________________
get all tweets, any one can do so
GET http://localhost:8080/api/tweets
authentication requier

__________________ 2  ______________________
tweets of specific user
anyone can do it
GET http://localhost:8080/api/tweets/harsh
authentication require


__________________ 3 ____________________________________________
for make tweet , authentication require
provide json
{
  "tweetContent": "My first3 tweet"
}

__________________ 4 _________________
update tweet, only own tweet
PUT http://localhost:8080/api/tweets
{
  "tweetId": 7,
  "tweetContent": "Updated my2 tweet!"
}

__________________ 5 _________________
delete tweet
DELETE http://localhost:8080/api/tweets/7

only own tweet

__________________ comments _________________________
__________________ 1 _________________
staff , anyone's tweet
user only itself
GET http://localhost:8080/api2/comments/user/reader

__________________ 2 _________________
comment of specific tweet
GET http://localhost:8080/api2/comments/tweet/4
staff only


__________________ 3 _________________
comment by specific id
GET http://localhost:8080/api2/comments/4

__________________ 4 _________________
make comment 
provide json and tweetid where you want to tweet
{
  "commentContent": "Nice tweet!",
  "tweet": {
    "tweetId": 5
  }
}

__________________ 5 _________________
update comment 
provide json and commentId of comment
{
    "commentId" : 6,
  "commentContent": "Nice updated comment on tweet!"

}
even staff can't modified you comment
__________________ 6 _________________
delete comment
DELETE http://localhost:8080/api2/comments/8
staff can delete too others

visa versa is not true


