package pl.dk.jcrplay;

import org.apache.jackrabbit.commons.JcrUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.jcr.GuestCredentials;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

@SpringBootTest
// from https://jackrabbit.apache.org/jcr/first-hops.html
class JackRabbitTests {

	@Test
	void firstHop() throws RepositoryException {
		Repository repository = JcrUtils.getRepository();
		Session session = repository.login(new GuestCredentials());
		try {
			String user = session.getUserID();
			String name = repository.getDescriptor(Repository.REP_NAME_DESC);
			System.out.println("-------------- Logged in as " + user + " to a " + name + " repository.");
		} finally {
			session.logout();
		}
	}


	@Test
	void secondHop() throws RepositoryException {
		Repository repository = JcrUtils.getRepository();
		Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
		try {
			Node root = session.getRootNode();

			// Store content
			Node hello = root.addNode("hello");
			Node world = hello.addNode("world");
			world.setProperty("message", "Hello, World!");
			session.save();

			// Retrieve content
			Node node = root.getNode("hello/world");
			System.out.println("--------- node path: " + node.getPath());
			System.out.println("--------- node property: " + node.getProperty("message").getString());

			// Remove content
			root.getNode("hello").remove();
			session.save();
		} finally {
			session.logout();
		}
	}
}
