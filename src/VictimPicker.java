import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VictimPicker extends JFrame {
    private JPanel participantPanel;
    private JPanel solvedPanel;
    private JPanel leaderboardPanel;
    private JTextField participantTextField;
    private JTextArea participantListTextArea;
    private JTextArea resultTextArea;
    private ArrayList<String> participantList;
    private Map<String, Integer> leaderboard;
    private String currentVolunteer;

    public VictimPicker() {
        participantList = new ArrayList<>();
        leaderboard = new HashMap<>();
        currentVolunteer = "";

        //main frame
        setTitle("Random Participant Selection");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //UI
        participantTextField = new JTextField(20);
        participantListTextArea = new JTextArea(10, 20);
        resultTextArea = new JTextArea(3, 20);
        JButton addParticipantButton = new JButton("Add Participant");
        JButton pickParticipantButton = new JButton("Pick Participant");
        JButton nextButton = new JButton("Next");

        //
        participantPanel = new JPanel();
        participantPanel.setLayout(new BoxLayout(participantPanel, BoxLayout.Y_AXIS));

        //
        participantPanel.add(new JLabel("Enter Participant Name:"));
        participantPanel.add(participantTextField);
        participantPanel.add(addParticipantButton);
        participantPanel.add(new JLabel("Participant List:"));
        participantPanel.add(new JScrollPane(participantListTextArea));
        participantPanel.add(pickParticipantButton);
        participantPanel.add(new JLabel("Result:"));
        participantPanel.add(new JScrollPane(resultTextArea));
        participantPanel.add(nextButton);

        // UI
        solvedPanel = new JPanel();
        solvedPanel.setLayout(new BoxLayout(solvedPanel, BoxLayout.Y_AXIS));

        JLabel questionLabel = new JLabel("Solved?");
        JCheckBox yesCheckBox = new JCheckBox("Yes");
        JCheckBox noCheckBox = new JCheckBox("No");
        JButton submitButton = new JButton("Submit");

        solvedPanel.add(questionLabel);
        solvedPanel.add(yesCheckBox);
        solvedPanel.add(noCheckBox);
        solvedPanel.add(submitButton);

        // UI
        leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        JButton showLeaderboardButton = new JButton("Show Leaderboard");

        leaderboardPanel.add(showLeaderboardButton);

        CardLayout cardLayout = new CardLayout();
        setLayout(cardLayout);

        add(participantPanel, "participantPanel");
        add(solvedPanel, "solvedPanel");
        add(leaderboardPanel, "leaderboardPanel");

        // ppl
        addParticipantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addParticipant();
            }
        });

        pickParticipantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pickParticipant();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(getContentPane(), "solvedPanel");
            }
        });

        // solved?
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmission(yesCheckBox.isSelected());
                cardLayout.show(getContentPane(), "leaderboardPanel");
            }
        });

        // leaderboard
        showLeaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLeaderboard();
                cardLayout.show(getContentPane(), "participantPanel");
            }
        });
    }

    private void addParticipant() {
        String participantName = participantTextField.getText().trim();
        if (!participantName.isEmpty()) {
            participantList.add(participantName);
            updateParticipantList();
            participantTextField.setText("");
        }
    }

    private void updateParticipantList() {
        StringBuilder participants = new StringBuilder();
        for (String participant : participantList) {
            participants.append(participant).append("\n");
        }
        participantListTextArea.setText(participants.toString());
    }

    private void pickParticipant() {
        if (!participantList.isEmpty()) {
            Random random = new Random();
            int selectedIndex = random.nextInt(participantList.size());
            currentVolunteer = participantList.get(selectedIndex);
            participantList.remove(selectedIndex);

            resultTextArea.setText("Thank you, " + currentVolunteer + ", for volunteering!");
            updateParticipantList();
        } else {
            resultTextArea.setText("No participants available.");
        }
    }

    private void handleSubmission(boolean solved) {
        // point given
        if (solved && !currentVolunteer.isEmpty()) {
            leaderboard.put(currentVolunteer, leaderboard.getOrDefault(currentVolunteer, 0) + 1);
        }
    }

    private void showLeaderboard() {
        StringBuilder leaderboardText = new StringBuilder("Leaderboard:\n");

        // disp ppl with points
        for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
            leaderboardText.append(entry.getKey()).append(": ").append(entry.getValue()).append(" points\n");
        }

        // disp ppl without points
        for (String participant : participantList) {
            if (!leaderboard.containsKey(participant)) {
                leaderboardText.append(participant).append(": 0 points\n");
            }
        }

        JOptionPane.showMessageDialog(this, leaderboardText.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VictimPicker().setVisible(true);
            }
        });
    }
}
