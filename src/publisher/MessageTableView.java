package publisher;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class MessageTableView extends JComponent {
	
	private ArrayList<Message> messages;
	
	private final Font globalFont = new Font("Avenir", Font.PLAIN, 12);
	private final Font globalFontB = new Font("Avenir", Font.BOLD, 12);
	
	public MessageTableView(ArrayList<Message> m) {
		this.messages = m;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		for ( int i = messages.size(); i > 0; i-- ) {
			addToTable(messages.get(i-1));
		}
	}
	
	private void addToTable(Message m) {
		JPanel messageView = new JPanel();
		messageView.setLayout(new GridLayout(1, 2));
		messageView.setMaximumSize(new Dimension(400, 25));
		
		// username
		JLabel uL = new JLabel("@" + m.getUserID());
		uL.setFont(globalFont);
		messageView.setAlignmentY(CENTER_ALIGNMENT);
		messageView.add(uL);
		
		// message
		JLabel mL = new JLabel(m.getMessage());
		mL.setFont(globalFont);
		mL.setAlignmentY(CENTER_ALIGNMENT);
		messageView.add(mL);
				
		this.add(messageView);
	}
	
}
