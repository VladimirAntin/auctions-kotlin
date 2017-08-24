


-- Users

INSERT INTO users(name,email,password,picture,address,phone,role) VALUES('admin','admin@email.com','pass','/images/profile.png',null,null,'admin')
INSERT INTO users(name,email,password,picture,address,phone,role) VALUES('owner','owner@email.com','pass','/images/profile.png',null,null,'owner')
INSERT INTO users(name,email,password,picture,address,phone,role) VALUES('bidder','bidder@email.com','pass','/images/profile.png',null,null,'bidder')

--Items

INSERT INTO items(name,description,picture,sold) VALUES('item1','description 1','/images/item.png',false)
INSERT INTO items(name,description,picture,sold) VALUES('item2','description 2','/images/item.png',true)
INSERT INTO items(name,description,picture,sold) VALUES('item3','description 3','/images/item.png',false)

-- Aucitons

INSERT INTO auctions(start_date,end_date,start_price,item_id,user_id) VALUES('2017-5-18','2017-5-22',500,1,1)
INSERT INTO auctions(start_date,end_date,start_price,item_id,user_id) VALUES('2017-5-19','2017-6-18',500,2,1)
INSERT INTO auctions(start_date,end_date,start_price,item_id,user_id) VALUES('2017-5-17','2017-6-18',500,3,1)


INSERT INTO auctions(start_date,start_price,item_id,user_id) VALUES('2017-5-18',500,1,2)
INSERT INTO auctions(start_date,start_price,item_id,user_id) VALUES('2017-5-19',500,2,2)
INSERT INTO auctions(start_date,start_price,item_id,user_id) VALUES('2017-5-17',500,3,2)


-- Bids

INSERT INTO bids(date_time,price,auction_id,user_id) VALUES('2017-5-21',5000,1,1)
INSERT INTO bids(date_time,price,auction_id,user_id) VALUES('2017-5-22',3000,2,2)
INSERT INTO bids(date_time,price,auction_id,user_id) VALUES('2017-5-23',4000,3,3)

INSERT INTO bids(date_time,price,auction_id,user_id) VALUES('2017-5-21',5000,1,2)
INSERT INTO bids(date_time,price,auction_id,user_id) VALUES('2017-5-22',3000,2,3)
INSERT INTO bids(date_time,price,auction_id,user_id) VALUES('2017-5-23',4000,3,1)

INSERT INTO bids(date_time,price,auction_id,user_id) VALUES('2017-5-21',5000,1,3)
INSERT INTO bids(date_time,price,auction_id,user_id) VALUES('2017-5-22',3000,2,1)
INSERT INTO bids(date_time,price,auction_id,user_id) VALUES('2017-5-23',4000,3,2)
