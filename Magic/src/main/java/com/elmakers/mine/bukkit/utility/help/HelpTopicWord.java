package com.elmakers.mine.bukkit.utility.help;

public class HelpTopicWord {
    private static final double RARITY_FACTOR = 3;
    private static final double TOPIC_RARITY_FACTOR = 5;
    private static final double LENGTH_FACTOR = 0.2;

    private final String word;
    private int count;
    private int topicCount;
    private Double weight;

    public HelpTopicWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public int getTopicCount() {
        return topicCount;
    }

    public void addTopic(int count) {
        this.topicCount++;
        this.count += count;
    }

    public double getWeight(Help help) {
        if (weight == null) {
            weight = computeWeight(help);
        }
        return weight;
    }

    private double computeWeight(Help help) {
        double rarityWeight = getRarityWeight(help.maxCount);
        double topicRarityWeight = getTopicWeight(help.maxTopicCount);
        double lengthWeight = getLengthWeight(word, help.maxLength);
        return rarityWeight * topicRarityWeight * lengthWeight;
    }

    protected double getRarityWeight(int maxCount) {
        double rarityWeight = 1.0 - ((double)count / (maxCount + 1));
        return Math.pow(rarityWeight, RARITY_FACTOR);
    }

    protected double getLengthWeight(String word, int maxLength) {
        double lengthWeight = (double)word.length() / maxLength;
        return Math.pow(lengthWeight, LENGTH_FACTOR);
    }

    protected double getTopicWeight(int maxTopicCount) {
        double topicRarityWeight = 1.0 - ((double)topicCount / (maxTopicCount + 1));
        return Math.pow(topicRarityWeight, TOPIC_RARITY_FACTOR);
    }
}