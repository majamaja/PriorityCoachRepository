CREATE TABLE static_pages
(
  id            UUID          NOT NULL PRIMARY KEY,
  name          VARCHAR(50)   NOT NULL UNIQUE,
  header        VARCHAR(100)  NOT NULL,
  content       VARCHAR(4000) NOT NULL,
  last_modified TIMESTAMP     NOT NULL DEFAULT now()
);

INSERT INTO static_pages (id, name, header, content)
    VALUES ('4c75739e-7bd4-4175-ab5f-599bfe598781', 'test', ' Test Page Header', 'Test Page Content');
