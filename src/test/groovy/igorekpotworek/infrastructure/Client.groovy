package igorekpotworek.infrastructure

import lombok.SneakyThrows

class Client {

    private Socket clientSocket
    private PrintWriter writer
    private BufferedReader reader

    @SneakyThrows
    String startConnection(String ip = "127.0.0.1", int port = 50000) {
        clientSocket = new Socket(ip, port)
        writer = new PrintWriter(clientSocket.getOutputStream(), true)
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        return reader.readLine()
    }

    @SneakyThrows
    String send(String message) {
        writer.println(message)
        return reader.readLine()
    }

    @SneakyThrows
    void stopConnection() {
        reader.close()
        writer.close()
        clientSocket.close()
    }
}
