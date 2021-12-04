---
title: Accessing the Server
description: Guide on how to connect to a Java http server.
---

# Accessing on Host Machine

When a server is started it is immediatly available at the local address. Only you use this address.

**Examples:** `localhost:8000`, `127.0.0.1`

# Accessing on Local Network

If permitted, the server is accessible to all computers on your immediate internet network at your [local ip](https://www.google.com/search?q=How+to+find+my+local+ip+address).

# Accessing Globally

For clients not connected to your network, they must use your [public ip address](https://www.whatismyip.com/what-is-my-public-ip-address/).

People outside your network can not connect unless you **port forward** the port the server is using. This process varies depending on your ISP and can often leave your network vulnerable to attackers. It is not suggested to this unless you know what you are doing.

You can learn to port forward [here](https://www.google.com/search?q=How+to+port+forward) (at your own risk).