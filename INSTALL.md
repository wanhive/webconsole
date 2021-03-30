# System requirement

* Java SE 8 or above
* Apache Tomcat 9 (tested) or [above](#apache-tomcat-10-setup)
* Postgresql database server 10 (tested) or above
* maven (for building from the source)

# Build and Install

To generate the WAR file, invoke:

```
mvn clean package

or 

mvn clean compile war:war
```

**REF**: (https://maven.apache.org/plugins/maven-war-plugin/usage.html)

## Tomcat setup

1. Download the most recent stable versions of the following libraries (JAR files)
    * [activation](https://mvnrepository.com/artifact/javax.activation/activation)
    * [javax-mail](https://mvnrepository.com/artifact/com.sun.mail/javax.mail)
    * [postgresql](https://mvnrepository.com/artifact/org.postgresql/postgresql)
2. Place the external libraries inside Apache Tomcat installation's **lib** directory.
3. Move the web archive (WAR) file to Apache Tomcat installation's **webapps** directory.

#### Apache Tomcat 10 setup

Quoting information from the [Apache Tomcat website](http://tomcat.apache.org/):

> Applications that run on Tomcat 9 and earlier will not run on Tomcat 10 without changes. Java EE applications designed for Tomcat 9 and earlier may be placed in the `$CATALINA_BASE/webapps-javaee` directory and Tomcat will automatically convert them to Jakarta EE and copy them to the webapps directory.

## Database setup
1. [Create a new database](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-postgresql-on-ubuntu-18-04).
2. Dump **db.sql** (included with this package) into the newly created database.
3. Update **server.xml** and **context.xml**.

```
psql <dbname> < db.sql
```

Add to the **GlobalNamingResources** element of **server.xml**

```
<Resource auth="Container" driverClassName="org.postgresql.Driver" global="jdbc/whwebc-ds" maxIdle="5" maxTotal="10" maxWaitMillis="-1" name="jdbc/whwebc-ds" password="<password>" type="javax.sql.DataSource" url="jdbc:postgresql://localhost:5432/<dbname>" username="<username>"/>
```

Add to **context.xml**

```
<ResourceLink name="jdbc/whwebc-ds" global="jdbc/whwebc-ds" auth="Container" type="javax.sql.DataSource" />
```

### User accounts

Execute the following database query to create an "activated" administrator account (replace *admin-email* and *admin-password* with valid email and password):

```
INSERT INTO wh_user (alias, email, password, type, status, token, tokentimestamp) VALUES('Admin', 'admin-email', crypt('admin-password', gen_salt('bf')), 3, 1, MD5(random()::text), now());
```

**Restart the Tomcat server.**

Log in to the web application with the given credentials and create additional users.

# Configuration

## Enable signing up (optional)

1. Receive SMTP settings from your email provider.
2. Update **server.xml** and **context.xml**.

Add something similar to the following inside the **GlobalNamingResources** element of **server.xml**

```
<Resource auth="Container" global="mail/whwebc-mail" mail.debug="false" mail.from="<email-address>" mail.port="465" mail.smtp.auth="true" mail.smtp.from="<email-address>" mail.smtp.host="<mail-smtp-host>" mail.smtp.ssl.enable="true" mail.smtp.user="<email-address>" mail.transport.protocol="smtps" name="mail/whwebc-mail" password="<password>" type="javax.mail.Session"/>
```

Add to **context.xml**

```
<ResourceLink name="mail/whwebc-mail" global="mail/whwebc-mail" auth="Container" type="javax.mail.Session" />
<Environment name="whwebc-allowsignup" value="true" type="java.lang.String" description="Allow new user sign up" />
```

## Enable Recaptcha3 (optional)

Add to **context.xml**

```
<Environment name="whwebc-captchasecret" value="<SECRET-KEY>" type="java.lang.String" description="Recaptcha v3 secret key" />
<Environment name="whwebc-captchasitekey" value="<SITE-KEY>" type="java.lang.String" description="Recaptcha v3 site key" />
```

## Enable Swagger API (optional)

Add to **context.xml**

```
<Environment name="whwebc-schemes" value="http,https" type="java.lang.String" description="Schemes for swagger API documentation" />
<Environment name="whwebc-hostname" value="<HOSTNAME>" type="java.lang.String" description="Host name for swagger API documentation" />
```

API documentation will become accessible at: *http(s)://HOSTNAME/api/swagger.json*

**NOTE**: HOSTNAME should be valid.
