vcl 4.0;

backend default {
  .host = "127.0.0.1";
  .port = "81";
}

backend plantilla {
  .host = "localhost";
  .port = "8080";
}

backend grafana {
  .host = "localhost";
  .port = "3000";
}

#sub vcl_fetch {
#  set beresp.ttl = 60s;     # Cache content for 60s
#}

sub vcl_recv {
  if (req.url ~ "^/grafana/") {
    set req.url = regsub(req.url, "^/grafana/", "/");
    set req.backend_hint = grafana;
  }  else {
    set req.backend_hint = plantilla;
  }
  return(pass); # Pass will return all content uncached
}
