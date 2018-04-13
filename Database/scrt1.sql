drop database if exists MUSICRT;
CREATE database MUSICRT;
USE MUSICRT;

CREATE TABLE USERS(
	userId INT(10) primary key auto_increment,
    username varchar(50) not null unique,
    pwd varchar(50) not null,
    fname varchar(80) not null,
    lname varchar(80) null,
    slocation varchar(500),
    imgLocation varchar(500),
    plattinumUser boolean
);

insert into USERS(username,pwd,fname,lname,plattinumUser)
	values('tanayssh', 'helloworld','tanay','shah',true),
		  ('avalante','swim','Alex', 'Valante',true),
          ('dshebib', 'csci201', 'Daniel', 'Shebib',false),
          ('joex','lol','Joe','x',false);

-- UPDATE USERS  SET profileImg = load_file('var/lib/d:/tanay/test.jpg') where USERS.userID = 1;

CREATE TABLE Friendship(
	fromUserId int(10) not null,
    toUserId int(10) not null,
    foreign key fk1(fromUserID) references USERS(userId),
    foreign key fk2(toUserID) references USERS(userId)
);


CREATE TABLE Chats(
	fromUserId int(10) not null,
    toUserId int(10) not null,
    chatLoc varchar(150),
    foreign key fk1(fromUserID) references USERS(userId),
    foreign key fk2(toUserID) references USERS(userId)
);

CREATE TABLE Lobbies(
	lobbyId int(10) primary key not null auto_increment,
    hostId int(10) not null,
    pswd varchar(10) not null,
    isPublic boolean,
    isActive boolean,
    foreign key fk1(hostId) references USERS(userId)
);

CREATE TABLE LobbuUsers(
	lobbyId int(10) not null,
    userId int(10) not null,
    foreign key fk1(lobbyId) references Lobbies(lobbyId),
    foreign key fk2(userId) references Users(userId)
);