package publisher.view;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import publisher.Topic;

/**
 * View of the table of topics on the Publisher GUI
 * @author camillemalonzo
 *
 */
public class TopicsTableView extends JComponent {
	private ArrayList<Topic> topics;	
	private final Font globalFont = new Font("Avenir", Font.PLAIN, 12);
	
	/**
	 * Pass the topics u and set up the GUI
	 * @param u
	 */
	public TopicsTableView(ArrayList<Topic> u) {
		this.topics = u;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		for ( int i = 0; i < topics.size(); i++ ) {
			addToTable(topics.get(i));
		}
	}
	
	/**
	 * Add each topic, t, to the table
	 * @param t Topic to add to table
	 */
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
	
	/**
	 * Create a random color
	 * @return
	 */
	private Color createRandomColor() {
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		return new Color(r, g, b);
	}
}
