package publisher;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import user.User;

public class TopicsTableView extends JComponent {
	private ArrayList<Topic> topics;
	
	private final Font globalFont = new Font("Avenir", Font.PLAIN, 12);
	private final Font globalFontB = new Font("Avenir", Font.BOLD, 12);
	
	public TopicsTableView(ArrayList<Topic> u) {
		this.topics = u;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		for ( int i = 0; i < topics.size(); i++ ) {
			addToTable(topics.get(i));
		}
	}
	
	private void addToTable(Topic t) {
		JPanel messageView = new JPanel();
		messageView.setLayout(new GridLayout(1, 2));
		messageView.setMaximumSize(new Dimension(500, 30));
		
		// username
		JLabel uL = new JLabel( "@" + t.getUserId() );
		uL.setFont(globalFont);
		uL.setForeground(createRandomColor());
		messageView.setAlignmentY(CENTER_ALIGNMENT);
		messageView.add(uL);
		
		// message
		JLabel mL = new JLabel(t.getAllSubscribers() );
		mL.setFont(globalFont);
		mL.setAlignmentY(CENTER_ALIGNMENT);
		messageView.add(mL);
				
		this.add(messageView);
	}
	
	private Color createRandomColor() {
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		return new Color(r, g, b);
	}
}
