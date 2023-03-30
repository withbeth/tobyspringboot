package withbeth.me.helloboot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

class HelloApiTest {

    @Test
    void helloApi() {
        // 이전장까지 수동으로 검증했던 방법을 자동화.
        // Request : http -v :8080/hello?name=withbeth
        // Response :
        // - status code should be 200
        // - content type should be json
        // - body should be "Hello, withbeth"
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(
            "http://localhost:8080/hello?name={name}", String.class, "withbeth");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // TODO : reponse content-type에 default charset설정이 들어가있어, MediaType.TEXT_PLAIN과 비교하지 못함.
        // TODO : 아마 다음장에서 default charset을 UTF-8로 바꾸고 다시 테스트
        //assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.TEXT_PLAIN);
        assertThat(response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)).startsWith(MediaType.TEXT_PLAIN_VALUE);
        assertThat(response.getBody()).isEqualTo("Hello, withbeth");
    }

}
