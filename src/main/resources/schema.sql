DROP TABLE IF EXISTS USERS, ITEMS, REQUESTS, COMMENTS, BOOKINGS CASCADE;

CREATE TABLE IF NOT EXISTS USERS (
  user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (user_id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS REQUESTS (
    request_id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description  VARCHAR(512) NOT NULL,
    requestor_id BIGINT REFERENCES USERS(user_id) ON DELETE CASCADE
    );

 CREATE TABLE IF NOT EXISTS ITEMS (
 	item_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 	owner_id BIGINT,
 	request_id BIGINT,
 	name VARCHAR(255) NOT NULL,
 	available   BOOLEAN      NOT NULL,
 	description VARCHAR(1000),
 	CONSTRAINT fk_items_to_users FOREIGN KEY(owner_id) REFERENCES users(user_id),
 	CONSTRAINT fk_requests_to_users FOREIGN KEY(request_id) REFERENCES REQUESTS(request_id)
 );

CREATE TABLE IF NOT EXISTS BOOKINGS (
    booking_id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone NOT NULL,
    item_id bigint REFERENCES ITEMS(item_id) ON DELETE CASCADE,
    booker_id bigint REFERENCES USERS(user_id) ON DELETE CASCADE,
    status varchar NOT NULL
    );

CREATE TABLE IF NOT EXISTS COMMENTS (
    comment_id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text varchar(255) NOT NULL,
    item_id bigint REFERENCES ITEMS(item_id) ON DELETE CASCADE,
    author_id bigint REFERENCES USERS(user_id) ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);