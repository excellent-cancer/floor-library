package gray.light.support.web;

import lombok.extern.apachecommons.CommonsLog;
import perishing.constraint.treasure.chest.ResourceTreasureChest;

import java.util.Optional;

import static gray.light.support.web.ResponseToClient.failWithMessage;
import static org.springframework.util.StringUtils.isEmpty;
import static org.springframework.util.StringUtils.trimAllWhitespace;

/**
 * 定义常用的提取器
 *
 * @author XyParaCrim
 */
@CommonsLog
public final class UnsatisfiedBodyExtractors {

    /**
     * 判断指定key的属性值是否为空
     */
    public static final UnsatisfiedBodyExtractor EXTRACT_NAME = (
            (key, properties) ->
                    isEmpty(properties.
                            computeIfPresent(key, (entryKey, entryValue) -> trimAllWhitespace((String) entryValue))) ?
                            Optional.of(failWithMessage("The property's value of " + key + " is empty.")) :
                            Optional.empty()
    );

    /**
     * 将指定key的属性值进行空格剔除，若未设置，则将其设置为空字符串
     */
    public static final UnsatisfiedBodyExtractor EXTRACT_DEFAULT = (
            (key, properties) -> {
                properties.compute(key, (entryKey, entryValue) -> isEmpty(entryValue) ? "" : trimAllWhitespace((String) entryValue));
                return Optional.empty();
            }
    );

    /**
     * 将指定key的属性转成long
     */
    public static final UnsatisfiedBodyExtractor EXTRACT_LONG = (
            (key, properties) ->
                    properties.computeIfPresent(key, (entryKey, entryValue) -> {
                        try {
                            return Long.valueOf((String) entryValue);
                        } catch (NumberFormatException e) {
                            log.error("Convert long project from client request");
                            return null;
                        }
                    }) == null ?
                            Optional.of(failWithMessage("The property's value of " + key + " is not long type")) :
                            Optional.empty()
    );

    public static final UnsatisfiedBodyExtractor EXTRACT_GIT = (
            (key, properties) ->
                    ResourceTreasureChest.isGitUrl((String) properties.get(key)) ?
                            Optional.empty() :
                            Optional.of(failWithMessage("The property's value of " + key + " is not a git url"))
    );

}
