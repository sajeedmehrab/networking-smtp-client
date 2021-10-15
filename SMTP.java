/**
 Log :
 1.Mail Sent 					OK
 2.Multiple Client 			OK
 3.Multi Line Message	OK
 4.Timeout 						NON_EXISTENT
 5.AttachmentPart 		NON_EXISTENT
 **/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws UnknownHostException, IOException {

        int sCount = 0;
        //String[] statename = {"Closed", "Begin", "Wait", "NoRcptEnvelop ", "RcptSet", "Mail Write", "DelReady", "DelAttempt", "\0"};
        String mailIDFrom = new String();
        String mailIDTo = new String();
        String userCommand = new String();
        String reply = new String();
        Scanner scan = new Scanner(System.in);
        System.out.println("***********Started***********");
        String mailServer = "webmail.buet.ac.bd";
        InetAddress mailHost = InetAddress.getByName(mailServer);
        InetAddress localHost = InetAddress.getLocalHost();
        Socket smtpSocket = new Socket(mailHost, 25);
        BufferedReader in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
        PrintWriter pr = new PrintWriter(smtpSocket.getOutputStream(), true);

        while (sCount != 6) {


            if (sCount == 0) {
                reply = in.readLine();
                System.out.println(reply);
                if (reply.charAt(0) == '2') {
                    sCount++;
                }
            } else if (sCount == 1) {

                System.out.println("Enter Command for HELO");
                userCommand = scan.nextLine();
                if (userCommand.equals("QUIT")) {

                    pr.println(userCommand);
                    pr.flush();
                    reply = in.readLine();
                    System.out.println(reply);
                    System.exit(0);
                } else if ((userCommand.equals("HELO"))) {

                    pr.println(userCommand + " " + localHost.getHostName());
                    pr.flush();
                    //               String welcome = new String();
                    //               welcome = in.readLine();
                    //               System.out.println(welcome);
                    //System.out.println("Command is" + userCommand);

                    reply = in.readLine();
                    System.out.println(reply);
                    if (reply.charAt(0) == '2')
                        sCount++;


                } else {
                    pr.println(userCommand);
                    pr.flush();
                    reply = in.readLine();
                    System.out.println(reply);
                    sCount = 1;
//                    System.out.println("Error: command not recognized");
                }

            } else if (sCount == 2) {

                System.out.println("Enter Command for Mail");
                userCommand = scan.nextLine();

                if (userCommand.equals("QUIT")) {
                    pr.println(userCommand);
                    pr.flush();
                    reply = in.readLine();
                    System.out.println(reply);
                    System.exit(0);
                } else if (userCommand.equals("MAIL")) {
                    System.out.println("From:");
                    mailIDFrom = scan.nextLine();

                    pr.println(userCommand + " from:<" + mailIDFrom + ">");
                    pr.flush();
                    reply = in.readLine();
                    System.out.println(reply);
                    if ((reply.charAt(0) == '2')) {
                        sCount++;
                    } else {
                        sCount = 2;
                        System.out.println("Error: command not recognized");
                    }

                } else {
                    pr.println(userCommand);
                    pr.flush();
                    reply = in.readLine();
                    System.out.println(reply);
                }
            } else if (sCount == 3) {

                System.out.println("Enter Command for RCPT");
                userCommand = scan.nextLine();
                reply = "";

                if (userCommand.equals("QUIT")) {
                    pr.println(userCommand);
                    pr.flush();
                    reply = in.readLine();
                    System.out.println(reply);
                    System.exit(0);
                } else if (userCommand.equals("RSET")) {
                    pr.println(userCommand);
                    pr.flush();
                    reply = in.readLine();
                    System.out.println(reply);
                    if ((reply.charAt(0) == '2'))
                        sCount = 2;
                } else if (userCommand.equals("RCPT")) {
                    {
                        System.out.println("To");
                        String TERMINATOR_STRING = "..";
                        String mail = new String();
                        String tempRep = new String();

                        while (!mail.equals(TERMINATOR_STRING)) {
                            mail = scan.nextLine();
                            if (!mail.equals(TERMINATOR_STRING)) {
                                mailIDTo += (mail + " \n");
                                pr.println("rcpt to:<" + mail + ">");
                                pr.flush();
                                reply = in.readLine();
                                if (reply.charAt(0) == '2') {
                                    sCount++;
                                }
                                tempRep += reply + "\n";
                            }
                        }
                        System.out.println(tempRep);


                    }
                } else {
                    pr.println(userCommand);
                    pr.flush();
                    reply = in.readLine();
                    System.out.println(reply);
                    sCount = 3;
                }

            } else if (sCount == 4) {
                System.out.println("Enter Command for DATA");
                userCommand = scan.nextLine();
                if (userCommand.equals("QUIT")) {
                    pr.println(userCommand);
                    pr.flush();
                    reply = in.readLine();
                    System.out.println(reply);
                    System.exit(0);
                } else if (userCommand.equals("RSET")) {
                    pr.println(userCommand);
                    pr.flush();
                    reply = in.readLine();
                    System.out.println(reply);
                    if ((reply.charAt(0) == '2'))
                        sCount = 2;
                } else if (userCommand.equals("DATA")) {
                    pr.println(userCommand);
                    pr.flush();
                    reply = in.readLine();
                    System.out.println(reply);
                    if ((reply.charAt(0) == '2' || reply.charAt(0) == '3')) {
                        sCount++;
                    } else {
                        sCount = 4;
                        System.out.println("Error: command not recognized");
                    }
                }
            } else if (sCount == 5) {
                System.out.println("Subject:");
                String messageSubject = new String();
                messageSubject = scan.nextLine();

                System.out.println("Enter Message:");

                String TERMINATOR_STRING = ".";
                String messageText = new String();
                String fetchText = new String();
                fetchText = scan.nextLine();
                messageText = fetchText;

                while (!fetchText.equals(TERMINATOR_STRING)) {
                    fetchText = scan.nextLine();
                    messageText += ('\n' + fetchText);
                }

                pr.println("Subject:" + messageSubject + "\n");
                pr.println("From:" + mailIDFrom + "\n");
                pr.println("To:" + mailIDTo + "\n");
                pr.println(messageText);
                pr.println();
                pr.flush();
                reply = in.readLine();
                System.out.println(reply);
                if ((reply.charAt(0) == '2' || reply.charAt(0) == '3')) {
                    sCount++;
                }
            }
        }
    }
}
