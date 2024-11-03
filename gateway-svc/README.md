# Configure minikube tunnel

```sh
minikube addons list

```

```sh
minikube addons enable ingress
```

Add this route to `/etc/hosts`
```sh
# /etc/hosts
127.0.0.1 mp3converter.com
```

```sh
minikube tunnel
```

