package org.numbnutz.tcpmon4g.net

class Server {
    Socket gatewaySocket
    ServerSocket serverSocket

    Server() {
        this.gatewaySocket = new Socket('192.168.1.1', 80)
    }

    void start(int port) {
        serverSocket = new ServerSocket(port)
        while (true) {
            new ClientHandler(serverSocket.accept(),
                    this.gatewaySocket)
        }
    }

    void stop() {
        serverSocket.close()
    }

    class ClientHandler extends Thread {
        Socket gatewaySocket
        Socket clientSocket
        PrintWriter out
        BufferedReader input

        ClientHandler(Socket clientSocket, Socket gatewaySocket) {
            this.clientSocket = clientSocket
            this.gatewaySocket = gatewaySocket
        }

        void run() {
            out = new PrintWriter(clientSocket.getOutputStream(), true)
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))

            try {
                def response = '';
                while ((response = input.readLine()) != null) {
                    response = input.readLine()
                    out.println(response)
                }
            } finally {
                input.close()
                out.close()
                clientSocket.close()
            }
        }
    }
}
