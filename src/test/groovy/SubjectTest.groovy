import spock.lang.*

class SubjectTest extends Specification {
	deff "a test"() {
		given:
		def subject = new Subject(x:5)

		expect:
		subject.x == 4
	}
}
