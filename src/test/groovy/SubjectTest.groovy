import spock.lang.Specification

class SubjectTest extends Specification {
	def "a test"() {
		given:
		def subject = new Subject(x:5)

		expect:
		subject.x == 5
	}
}
