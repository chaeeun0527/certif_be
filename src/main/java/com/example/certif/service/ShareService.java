package com.example.certif.service;

import com.example.certif.dto.SharePostResponseDto;
import com.example.certif.entity.SharePostEntity;
import com.example.certif.repository.SharePostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShareService {

    @Autowired
    private SharePostRepository shareRepository;

    public List<SharePostEntity> index() {
        return shareRepository.findAll();
    }

    public SharePostEntity show(Long postId) {
        return shareRepository.findById(postId).orElse(null);
    }

    public SharePostEntity create(SharePostResponseDto dto) {
        SharePostEntity shareEntity = dto.toEntity();

        if(shareEntity.getId() != null){
            return null;
        }
        return shareRepository.save(shareEntity);
    }

    public SharePostEntity update(Long postId, SharePostResponseDto dto) {
        SharePostEntity shareEntity = dto.toEntity();
        SharePostEntity target = shareRepository.findById(postId).orElse(null);
        if(target == null || postId != shareEntity.getId()){
            log.info("잘못된 요청입니다");
            return null;
        }

        target.patch(shareEntity);
        SharePostEntity updated = shareRepository.save(target);
        return updated;
    }

    public SharePostEntity delete(Long postId) {
        SharePostEntity target = shareRepository.findById(postId).orElse(null);
        if(target == null){
            return null;
        }
        shareRepository.delete(target);
        return target;
    }
}
