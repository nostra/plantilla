<!--  -*- markdown -*- -->

# Plantilla<br/> project setup and tooling


----

<!-- Build with `landslide config.cfg --copy-theme` -->


# Agenda

* To tell about best practices for setting up a project - according to me.
  Each tech item is really worthy of its own presentation, though...
* Try to provide takeaway points
* Go beyond the presentation of the application framework to focus on how you get your work into production
* Strategies for setting up environments / routines
* Provide a framework which (hopefully) can be used to get your app into production quickly
    * Note that there are many moving parts, and something may need to be changed
* Check this framework out with:

```
git clone git@github.com:nostra/plantilla.git
```

.notes:   Presenter notes present
# Presenter notes

presenter notes


-----

# Items

* set up github bitbucket account
* gradle and project setup
* dropwizard
* sonarqube
<!-- * jenkins -->
* docker + graphite + grafana ++
* package or fat jar
* deployment in the cloud (here: digital ocean)
* ansible


-----

# Git

* No reason to keep subversion, nor CVS (the horror)
* Annoying if someone fills up the repository with binary files, though
* <a href="http://www.github.com/" target="win">github</a>
* <a href="https://www.bitbucket.com/" target="win">bitbucket</a>
* <a href="https://about.gitlab.com/" target="win">gitlab</a>
* Undoubtedly other providers
* Providers differ in work flow and management tools
* Possible to live dangerously with git-svn. Tip: You need a good plan and failure-proof routines
* Bake your own:

<pre>
# Creating directory in which the git projects shall reside
mkdir -p /opt/git/static/presentations.git
# The base directory is sticky, so the group is OK - but without rights
chmod -R g+w /opt/git/static
cd /opt/git/static/presentations.git
# Initialize directory
git init --bare --shared
</pre>


-----

# Gradle

* Quicker than maven
* Somewhat less obtuse
* Android studio uses gradle
* Topic for later: <a href="https://docs.gradle.org/current/userguide/organizing_build_logic.html"
  target="win">Separating out common configuration</a>
* Maven-like profiles supported as
  <a href="https://gradle.org/feature-spotlight-gradles-support-maven-pom-profiles/" target="win">programmed functionality</a>
* `$HOME/.gradle/gradle.properties` for defaults, most often <code>org.gradle.daemon=true</code>
* <code>gradle --continuous build</code>
* Missing <code>mvn dependency:tree</code> and similar. May come in later versions
* Remember to add gradle wrapper to `.gitignore`
* ... but need wrapper, as in `./gradlew wrapper` (in order to get 2.8-rc1 or greater)


-----

# Gradle and the release process

* Releasing is:
    * Update the version (from snapshot to release version)
    * Tag your repository with <a href="http://semver.org" target="win">a semver version</a>
    * Build
    * Upload to remote repository
    * Update the version to the next snapshot
* You only put released versions into production
* Maven has a rather good release plugin
* The release part of gradle has been particularly painful to get to work
    * **Finally** managed to make a setup which works OK with remote ssh repository
    * Supported by release candidate, gradle-2.8-rc1
* (Demo of release will be shown in later slide)

-----

#  DropWizard

* Best practices organized:
    * Jetty, Jackson, Jersey, Metrics, ...
* Does not like:
    * Springframework
    * More than one configuration file
    * To roll your logs sensibly
* Might be a challenge to use if porting legacy code
* Alternatives:
     * <a href="http://projects.spring.io/spring-boot/" target="win">Spring boot</a>
     * <a href="http://sparkjava.com/" target="win">sparkjava</a>
* Quirky when developing for web due to reload-issues
     * Practice your shortcut skills
* Freemarker is easily included, though
* Easy to create docker image

<!--
# Java-8
* Makes Java fun again
-->
-----

# The demo project

* (Does <a href="http://localhost:8080" target="win">not really do anything</a> useful...)

-----

# Docker

* Containers rock. Mostly.
* Except during restart
* Except when you have both ip4 and ip6
* Except when you have several of them that need to talk to each other
    * Need government (such as kubernetes)
* Painful when images go haywire
* (Examples of setup to follow)
* Strategies for storage backup
    * Mount local directory
    * Use data image
* There exist <a href="https://hub.docker.com/" target="win">oodles of images</a>
* In production: **Always** `sudo docker`, because you can
  <a href="https://github.com/chrisfosterelli/dockerrootplease" target="win">obtain root</a> from a docker group
* Nice to get technologies you want to examine quickly set up
-----

# Sonarqube

* <a href="http://sonarqube.org/" target="win">sonarqube.org</a> measures
your build quality, but is a hassle to maintain manually
* docker-compose to the rescue:

```
git clone git://github.com/harbur/docker-sonarqube.git
cd docker-sonarqube
docker-compose up
```

* In your project (which contains the correct setup): `./gradlew sonarqube`
* Watch output on <a href="http://localhost:9000/" target="win">http://localhost:9000/</a>
* You want to have your config files stored separately for later import

<!--

-----

# Sonarqube as plain docker commands

sudo docker run -d --name sonarqube -p 9000:9000 harbur/sonarqube:latest

```
sudo docker run -d --name postgresql -p 5432:5432 \
-e POSTGRESQL_USER=sonar  \
-e POSTGRESQL_PASS=xaexohquaetiesoo \
-e POSTGRESQL_DB=sonar  \
       orchardup/postgresql:latest

sudo docker run -d --name sonarqube -p 9000:9000 \
--link postgresql:postgresql \
-e DB_USER=sonar \
-e DB_PASS=xaexohquaetiesoo \
-e DB_NAME=sonar \
       harbur/sonarqube:latest
```
-->
-----

# Graphite + grafana

* Least effort setup for (local) metrics

<pre>
sudo docker run -d --name graphite -p 80:80 -p 2003:2003 \
       -p 8125:8125/udp hopsoft/graphite-statsd
sudo docker run -d --name grafana -p 3000:3000 \
       -e "GF_SECURITY_ADMIN_PASSWORD=secret" grafana/grafana
sudo docker inspect -f '{{ .NetworkSettings.IPAddress }}' graphite
</pre>

* Links:
    * <a href="http://localhost:80" target="win">Graphite</a>
    * <a href="http://localhost:3000" target="win">Grafana</a> (password <i>secret</i>)
* Would need to invest time to get the setup streamlined
* Add test element with:
<pre>
echo -n "test.demo.hepp 1 `date +%s`" | nc -w 1 -u localhost 2003
</pre>


<!--
# jenkins

* Continuous build
* https://guides.codepath.com/android/Building-Gradle-Projects-with-Jenkins-CI
-->

-----

# Digital Ocean

* Many places to put cloud applications - Digital Ocean was chosen due to static IP
* <a href="https://www.digitalocean.com" target="win">https://www.digitalocean.com</a>
* (demo setting up project)
* ssh setup:

<pre>
Host plantilla	188.166.22.170
    HostName 188.166.22.170
    IdentityFile ~/.ssh/digitalocean
    User root
</pre>

* You should be able to ssh into the image: `ssh plantilla`

-----

# Finishing touches on image

* Note that plantilla runs on Ubuntu 14.04.3 LTS, which has the code
  name _Trusty Tahr_.
* Manually set up FW (could have been done with ansible)

<pre>
ufw allow ssh
ufw allow 80/tcp
ufw show added
ufw enable
ufw status
</pre>

-----

# Ansible

* You want to be able to have a VM configuration which is reproducible
* A tool similar to puppet, chef, salt and others
* Agent-less, which means you don't have to run it on the server
* (But you can run it on a server, though, for pull-style update)
* Easy to test changes in configuration, as you can just choose a sub-file
* sudo emacs -nw /etc/ansible/hosts (or where determined by `ANSIBLE_HOSTS`)
* You can, of course, use ansible to control the dev setup on your machine too

# Presenter notes

* Ansible and security
* http://docs.ansible.com/ansible/playbooks_best_practices.html
* Puppet needs an agent. This is a SPOF

-----

# Ansible galaxy

* Get configurations for non-default elements from
  <a href="https://galaxy.ansible.com/" target="win">Ansible Galaxy</a>
* Playbooks are distribution dependent, which is why we remember Ubuntu Trusty Tahr
* Install element with:

<pre>
sudo ansible-galaxy install malk.java8-oracle
sudo ansible-galaxy install geerlingguy.varnish
sudo ansible-galaxy install tersmitten.pip
</pre>

* (Demo: Ansible server setup files)
* `ansible-playbook --vault-password-file=~/.vault_password server_setup.yml  `
* `ansible-vault --vault-password-file=~/.vault_password edit vars/secret.yml `

# Presenter notes

Including reference to vault-pw file makes sure you don't forget it when you
need it.
Secrets are used to avoid checking passwords and similar into git

-----

# Release and deploy

* (Note: Egg and chicken problem with current deployment setup)
* creating a fat jar
* gradle hate:
    * First create wrapper: `gradle wrapper` (needed as 2.7 does not support release)
    * Then do release: `./gradlew release`
    * (or just deploy snapshot: `./gradlew afterReleaseBuild`, but then you need to hack the deployment setup)
    * Update ansible/vars/plantilla_version as applicable
    * Sigh: Both tar and zip archives were transferred
* In a real project, the release process needs to be scripted as a single (shell) command    
* deployment on digital ocean
    * `ansible-playbook --vault-password-file=~/.vault_password deploy.yml  `

----

# Recap

* Standardize setup
* What cannot be done in a terminal is not worth doing: <b>Script everything</b>
* Think ahead


# Presenter notes

No comment

-----

# Questions?<!--img src="images/amedia_questions.jpg" width="800" align="center" style="float: top;" /-->
