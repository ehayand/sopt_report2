package org.sopt.report2.api;

import org.sopt.report2.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ehay@naver.com on 2018-10-13
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@RestController
public class UserController {

    private final static List<User> userList = new LinkedList<>();
    private static int autoIncrementId = 1;

    /**
     * 요구사항 1 : 현재 시간 반환
     * @return 현재 시간
     */
    @GetMapping("")
    public String getTime() {
        LocalDateTime time = LocalDateTime.now();
        return time.getHour() + "시 " + time.getMinute() + "분 " + time.getSecond() + "초";
    }

    /**
     * 요구사항 2 : 현재 리스트에 등록된 모든 회원 데이터 반환
     * 요구사항 3 : 현재 리스트에서 같은 이름의 회원이 있는지 검색, 있으면 회원 데이터 반환, 없으면 "없습니다" 반환
     * 요구사항 4 : 현재 리스트에서 같은 파트의 회원이 있는지 검색, 있으면 회원 데이터 반환, 없으면 "없습니다" 반환
     * @return 회원 리스트 or "없습니다"
     */
    @GetMapping("/users")
    public Object getUserList(
            @RequestParam(value = "name", defaultValue = "null") final String name,
            @RequestParam(value = "part", defaultValue = "null") final String part) {

        // 요구사항 2
        if ("null".equals(name) && "null".equals(part)) {
            if (userList.isEmpty()) return "없습니다";
            return userList;
        }

        List<User> list = new ArrayList<>();

        // 요구사항 3
        if ("null".equals(part)) {
            for (final User target : userList) {
                if (name.equalsIgnoreCase(target.getName())) list.add(target);
            }
        }

        // 요구사항 4
        else if ("null".equals(name)) {
            for (final User target : userList) {
                if (part.equalsIgnoreCase(target.getPart())) list.add(target);
            }
        }

        // 추가사항
        else {
            for (final User target : userList) {
                if (name.equalsIgnoreCase(target.getName()) && part.equalsIgnoreCase(target.getPart()))
                    list.add(target);
            }
        }

        if (list.isEmpty()) return "없습니다";

        return list;
    }

    /**
     * 요구사항 5 : 현재 리스트에서 회원 id값으로 회원 검색, 있으면 회원 데이터 반환, 없으면 "없습니다" 반환
     * @param id
     * @return 회원정보 or "없습니다"
     */
    @GetMapping("/users/{user_idx}")
    public Object findById(@PathVariable(value = "user_idx", required = true) final int id) {
        User user = null;

        for (final User target : userList) {
            if ((target.getUser_idx() == id)) user = target;
        }

        if (user == null) return "없습니다";

        return user;
    }

    /**
     * 요구사항 6 : 회원 저장
     * @param user
     * @return 회원정보 + 성공 메세지
     */
    @PostMapping("/users")
    public String insertUser(@RequestBody final User user) {
        user.setUser_idx(autoIncrementId++);
        userList.add(user);

        return user.getUser_idx() + " " +
                user.getName() + " " +
                user.getPart() + " insert Success!";
    }

    /**
     * 요구사항 7 : 회원 정보 수정
     * @param id
     * @param user
     * @return 성공 메세지
     */
    @PutMapping("/users/{user_idx}")
    public String updateUser(@PathVariable(value = "user_idx", required = true) final int id,
                             @RequestBody final User user) {

        boolean flag = false;

        for (final User target : userList) {
            if (target.getUser_idx() == id) {
                target.setName(user.getName());
                target.setPart(user.getPart());

                flag = true;
                break;
            }
        }

        if (!flag) return "입력한 id의 유저가 없습니다";
        return "update Success!";
    }

    /**
     * 요구사항 8 : 회원 삭제
     * @param id
     * @return 성공 메세지
     */
    @DeleteMapping("/users/{user_idx}")
    public String deleteUser(
            @PathVariable(value = "user_idx", required = true) final int id) {

        int index = 0;

        for (final User target : userList) {
            if (target.getUser_idx() == id) break;
            index++;
        }

        if (index >= userList.size()) return "입력한 id의 유저가 없습니다";

        userList.remove(index);

        return "delete Success!";
    }
}
