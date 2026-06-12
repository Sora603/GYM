package com.gym.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gym.entity.MembershipCard;
import com.gym.entity.SalesRecord;
import com.gym.entity.User;
import com.gym.entity.UserCard;
import com.gym.mapper.MembershipCardMapper;
import com.gym.mapper.SalesRecordMapper;
import com.gym.mapper.UserCardMapper;
import com.gym.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CardService {

    @Resource
    private MembershipCardMapper cardMapper;

    @Resource
    private UserCardMapper userCardMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private SalesRecordMapper salesRecordMapper;

    // 获取所有卡类型
    public List<MembershipCard> getAllCards() {
        return cardMapper.selectList(null);
    }

    // 购买卡（支持日期叠加）
    @Transactional
    public UserCard buyCard(Long userId, Long cardId) {
        MembershipCard card = cardMapper.selectById(cardId);
        if (card == null) {
            throw new RuntimeException("卡类型不存在");
        }

        // 查找用户所有卡的最晚到期日，叠加延长
        List<UserCard> allCards = userCardMapper.findByUserId(userId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = allCards.stream()
            .map(UserCard::getExpireTime)
            .max(LocalDateTime::compareTo)
            .map(latest -> latest.isAfter(now) ? latest : now)
            .orElse(now);

        UserCard userCard = new UserCard();
        userCard.setUserId(userId);
        userCard.setCardId(cardId);
        userCard.setCardName(card.getName());
        userCard.setBuyTime(startTime);
        userCard.setExpireTime(startTime.plusDays(card.getDurationDays()));
        userCard.setStatus(1);
        userCardMapper.insert(userCard);

        // 如果用户还没有VIP号，购卡时根据手机号生成
        User user = userMapper.selectById(userId);
        if (user != null && user.getVipCardNo() == null) {
            user.setVipCardNo("VIP" + user.getPhone());
            userMapper.updateById(user);
        }

        // 记录销售
        SalesRecord record = new SalesRecord();
        record.setUserId(userId);
        record.setItemType("CARD");
        record.setItemName(card.getName());
        record.setAmount(card.getPrice());
        record.setCreateTime(LocalDateTime.now());
        salesRecordMapper.insert(record);

        return userCard;
    }

    // 获取用户的所有卡
    public List<UserCard> getUserCards(Long userId) {
        return userCardMapper.findByUserId(userId);
    }

    // 获取用户有效卡
    public List<UserCard> getValidUserCards(Long userId) {
        return userCardMapper.findValidByUserId(userId);
    }

    // 修复用户购卡记录的叠加（按购买时间排序，后续卡的buyTime改为前一张卡的expireTime）
    @Transactional
    public void fixCardStacking(Long userId) {
        List<UserCard> cards = userCardMapper.findByUserId(userId);
        if (cards.size() < 2) return;
        cards.sort((a, b) -> a.getBuyTime().compareTo(b.getBuyTime()));
        UserCard prev = cards.get(0);
        for (int i = 1; i < cards.size(); i++) {
            UserCard curr = cards.get(i);
            MembershipCard cardType = cardMapper.selectById(curr.getCardId());
            long durationDays = cardType != null ? cardType.getDurationDays() : 365;
            curr.setBuyTime(prev.getExpireTime());
            curr.setExpireTime(prev.getExpireTime().plusDays(durationDays));
            userCardMapper.updateById(curr);
            prev = curr;
        }
    }
}
