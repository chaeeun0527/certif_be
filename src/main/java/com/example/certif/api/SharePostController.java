package com.example.certif.api;

import com.example.certif.dto.SharePostResponseDto;
import com.example.certif.entity.SharePostEntity;
import com.example.certif.service.ShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class SharePostController {
    @Autowired
    private ShareService shareService;

    //GET - 전체 목록 조회
    @GetMapping("/api/share")
    public List<SharePostEntity> index(){
        return shareService.index();
    }
    //GET - 특정 글 조회
    @GetMapping("/api/share/{postId}")
    public SharePostEntity show(@PathVariable Long postId){
        return shareService.show(postId);
    }

    //POST - 게시글 등록
    @PostMapping("/api/share")
    public ResponseEntity<SharePostEntity> create(@RequestBody SharePostResponseDto dto){
        SharePostEntity created = shareService.create(dto);
        return (created != null) ?
                ResponseEntity.status(HttpStatus.OK).body(created) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //PATCH - 글 수정
    @PatchMapping("/api/share/{postId}")
    public ResponseEntity<SharePostEntity> update(@PathVariable Long postId, @RequestBody SharePostResponseDto dto){
        SharePostEntity updated = shareService.update(postId, dto);
        return (updated != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //DELETE - 글 삭제
    @DeleteMapping("/api/share/{postId}")
    public ResponseEntity<SharePostEntity> delete(@PathVariable Long postId){
        SharePostEntity deleted = shareService.delete(postId);
        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
