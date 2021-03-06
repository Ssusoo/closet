package com.ssu.closet.service.cody;

import com.ssu.closet.domain.cody.Cody;
import com.ssu.closet.domain.cody.CodyForm;
import com.ssu.closet.domain.cody.CodyRepository;
import com.ssu.closet.domain.member.Member;
import com.ssu.closet.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodyService {

    private final CodyRepository codyRepository;
    private final MemberRepository memberRepository;

    public Cody createNewCody(Member member, CodyForm codyForm) {

        if (codyForm.getTopId()==null) {
            codyForm.setTopId(0L);
        }

        Cody cody = Cody.builder()
                .member(member)
                .topId(codyForm.getTopId())
                .bottomId(codyForm.getBottomId())
                .shoesId(codyForm.getShoesId())
                .backgroundId(codyForm.getBackgroundId())
                .topSize(codyForm.getTopSize())
                .bottomSize(codyForm.getBottomSize())
                .shoesSize(codyForm.getShoesSize())
                .build();

        if (codyForm.getOuterId()!=null) {
            cody.setOuterId(codyForm.getOuterId());
            cody.setOuterSize(codyForm.getOuterSize());
        }

        if (codyForm.getAccId()!=null) {
            cody.setAccId(codyForm.getAccId());
            cody.setAccSize(codyForm.getAccSize());
        }

        codyRepository.save(cody);

        return cody;

    }

    public List<Cody> getCodyList(Member member) {
        List<Cody> codyList = codyRepository.findAllByMember(member);

        return codyList;
    }

    public List<Cody> getAllList() {
        List<Cody> codyList = codyRepository.findAll();

        return codyList;
    }

//    @Transactional
//    public boolean addCodyLike(Member member, Long codyId) {
//        if (member == null){
//            throw new UsernameNotFoundException("wrong user");
//        }
//        Optional<Cody> optional = codyRepository.findById(codyId);
//        Cody cody = optional.get();
//
//        // codyId??? ????????? ??????
//        if(cody == null){
//            throw new IllegalArgumentException("wrong cody info!");
//        }
//
//        // member??? detach ?????? -> Repo??? ?????? Select????????? ??? ??? ???????????? ??????.
//        member = memberRepository.getOne(member.getId()); // detach -> persist ????????? ????????????.
//        List<Cody> codyLikeList = member.getCodyLikes();
//
//        if(codyLikeList.contains(cody)){
//            codyLikeList.remove(cody);
//            return false;
//        }
//
//        codyLikeList.add(cody);
//        return true;
//    }
}