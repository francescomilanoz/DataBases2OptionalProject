CREATE DATABASE  IF NOT EXISTS `db_marketing_app` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_marketing_app`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Users table
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `active` bool NOT NULL,
  `points` int NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`user_id`), 
  UNIQUE(`username`), 
  UNIQUE(`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Dumping data for table `user`
--

 LOCK TABLES `user` WRITE;
 /*!40000 ALTER TABLE `user` DISABLE KEYS */;
 INSERT INTO `user` VALUES (1, 'admin', 'admin', 'admin', true, 0, 'Admin'), (2, 'user', 'user', 'user', true, 0, 'User');
 /*!40000 ALTER TABLE `user` ENABLE KEYS */;
 UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `product_date` date NOT NULL,
  `product_name` varchar(45) NOT NULL,
  `product_image` longblob, 
  PRIMARY KEY (`product_id`), 
  UNIQUE(`product_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `question_id` int NOT NULL AUTO_INCREMENT,
  `question_text` varchar(45) DEFAULT NULL,
  `question_type` varchar(45) DEFAULT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`question_id`),
  CONSTRAINT `question_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer` (
  `answer_id` int NOT NULL AUTO_INCREMENT,
  `answer_text` varchar(45) DEFAULT NULL,
  `user_id` int NOT NULL,
  `question_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`answer_id`), 
  CONSTRAINT `answer_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE, 
  CONSTRAINT `answer_question_id` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`) ON DELETE CASCADE ON UPDATE CASCADE, 
  CONSTRAINT `answer_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE, 
  UNIQUE(`question_id`,  `user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `review_text` varchar(45) DEFAULT NULL,
  `product_id` int NOT NULL, 
  `user_id` int NOT NULL, 
  PRIMARY KEY (`review_id`), 
  CONSTRAINT `review_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE, 
  CONSTRAINT `review_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `login`
--

DROP TABLE IF EXISTS `login_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login_record` (
  `login_id` int NOT NULL AUTO_INCREMENT,
  `login_timestamp` timestamp NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`login_id`), 
  CONSTRAINT `login_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `leaderboard`
--

USE `db_marketing_app`;

DROP TABLE IF EXISTS `leaderboard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leaderboard` (
  `leaderboard_id` int NOT NULL AUTO_INCREMENT,
  `leaderboard_timestamp` timestamp NOT NULL,
  `daily_points` int NOT NULL,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`leaderboard_id`), 
  UNIQUE (`user_id`, `product_id`),
  CONSTRAINT `leaderboard_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `leaderboard_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `offensive_words`
--

DROP TABLE IF EXISTS `offensive_words`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `offensive_words` (
  `offensive_word_id` int NOT NULL AUTO_INCREMENT,
  `offensive_word_text` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`offensive_word_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



INSERT INTO offensive_words (offensive_word_text) VALUES
    ('2g1c'),
    ('2 girls 1 cup'),
    ('acrotomophilia'),
    ('alabama hot pocket'),
    ('alaskan pipeline'),
    ('anal'),
    ('anilingus'),
    ('anus'),
    ('apeshit'),
    ('arsehole'),
    ('ass'),
    ('asshole'),
    ('assmunch'),
    ('auto erotic'),
    ('autoerotic'),
    ('babeland'),
    ('baby batter'),
    ('baby juice'),
    ('ball gag'),
    ('ball gravy'),
    ('ball kicking'),
    ('ball licking'),
    ('ball sack'),
    ('ball sucking'),
    ('bangbros'),
    ('bareback'),
    ('barely legal'),
    ('barenaked'),
    ('bastard'),
    ('bastardo'),
    ('bastinado'),
    ('bbw'),
    ('bdsm'),
    ('beaner'),
    ('beaners'),
    ('beaver cleaver'),
    ('beaver lips'),
    ('bestiality'),
    ('big black'),
    ('big breasts'),
    ('big knockers'),
    ('big tits'),
    ('bimbos'),
    ('birdlock'),
    ('bitch'),
    ('bitches'),
    ('black cock'),
    ('blonde action'),
    ('blonde on blonde action'),
    ('blowjob'),
    ('blow job'),
    ('blow your load'),
    ('blue waffle'),
    ('blumpkin'),
    ('bollocks'),
    ('bondage'),
    ('boner'),
    ('boob'),
    ('boobs'),
    ('booty call'),
    ('brown showers'),
    ('brunette action'),
    ('bukkake'),
    ('bulldyke'),
    ('bullet vibe'),
    ('bullshit'),
    ('bung hole'),
    ('bunghole'),
    ('busty'),
    ('butt'),
    ('buttcheeks'),
    ('butthole'),
    ('camel toe'),
    ('camgirl'),
    ('camslut'),
    ('camwhore'),
    ('carpet muncher'),
    ('carpetmuncher'),
    ('chocolate rosebuds'),
    ('circlejerk'),
    ('cleveland steamer'),
    ('clit'),
    ('clitoris'),
    ('clover clamps'),
    ('clusterfuck'),
    ('cock'),
    ('cocks'),
    ('coprolagnia'),
    ('coprophilia'),
    ('cornhole'),
    ('coon'),
    ('coons'),
    ('creampie'),
    ('cum'),
    ('cumming'),
    ('cunnilingus'),
    ('cunt'),
    ('dafuq'),
    ('dank'),
    ('darkie'),
    ('date rape'),
    ('daterape'),
    ('deep throat'),
    ('deepthroat'),
    ('dendrophilia'),
    ('dick'),
    ('dork'),
    ('dildo'),
    ('dingleberry'),
    ('dingleberries'),
    ('dips hit'),
    ('dirty pillows'),
    ('dirty sanchez'),
    ('doggie style'),
    ('doggiestyle'),
    ('doggy style'),
    ('doggystyle'),
    ('dog style'),
    ('dolcett'),
    ('domination'),
    ('dominatrix'),
    ('dommes'),
    ('donkey punch'),
    ('double dong'),
    ('double penetration'),
    ('douche'),
    ('douchebag'),
    ('dumbass'),
    ('dp action'),
    ('dry hump'),
    ('dvda'),
    ('eat my ass'),
    ('ecchi'),
    ('ejaculation'),
    ('erotic'),
    ('erotism'),
    ('escort'),
    ('eunuch'),
    ('fag'),
    ('faggot'),
    ('fecal'),
    ('felch'),
    ('fellatio'),
    ('feltch'),
    ('female squirting'),
    ('femdom'),
    ('figging'),
    ('fingerbang'),
    ('fingering'),
    ('fisting'),
    ('foot fetish'),
    ('footjob'),
    ('frotting'),
    ('fuck'),
    ('fuck buttons'),
    ('fuckin'),
    ('fucking'),
    ('fucktards'),
    ('fudge packer'),
    ('fudgepacker'),
    ('futanari'),
    ('gang bang'),
    ('gay sex'),
    ('genitals'),
    ('giant cock'),
    ('girl on'),
    ('girl on top'),
    ('girls gone wild'),
    ('goatcx'),
    ('goatse'),
    ('god damn'),
    ('gokkun'),
    ('golden shower'),
    ('goodpoop'),
    ('goo girl'),
    ('goregasm'),
    ('grope'),
    ('group sex'),
    ('g-spot'),
    ('guro'),
    ('hand job'),
    ('handjob'),
    ('hard core'),
    ('hardcore'),
    ('hentai'),
    ('hoe'),
    ('homoerotic'),
    ('honkey'),
    ('hooker'),
    ('hot carl'),
    ('hot chick'),
    ('how to kill'),
    ('how to murder'),
    ('huge fat'),
    ('humping'),
    ('incest'),
    ('intercourse'),
    ('jack off'),
    ('jail bait'),
    ('jailbait'),
    ('jelly donut'),
    ('jerk off'),
    ('jigaboo'),
    ('jiggaboo'),
    ('jiggerboo'),
    ('jizz'),
    ('juggs'),
    ('kike'),
    ('kinbaku'),
    ('kinkster'),
    ('kinky'),
    ('knobbing'),
    ('leather restraint'),
    ('leather straight jacket'),
    ('lemon party'),
    ('lolita'),
    ('lovemaking'),
    ('make me come'),
    ('male squirting'),
    ('masturbate'),
    ('menage a trois'),
    ('milf'),
    ('missionary position'),
    ('motherfucker'),
    ('mound of venus'),
    ('mr hands'),
    ('muff diver'),
    ('muffdiving'),
    ('nambla'),
    ('nawashi'),
    ('negro'),
    ('neonazi'),
    ('nigga'),
    ('nigger'),
    ('nig nog'),
    ('nimphomania'),
    ('nipple'),
    ('nipples'),
    ('nsfw images'),
    ('nude'),
    ('nudity'),
    ('nympho'),
    ('nymphomania'),
    ('octopussy'),
    ('omorashi'),
    ('one cup two girls'),
    ('one guy one jar'),
    ('orgasm'),
    ('orgy'),
    ('paedophile');
INSERT INTO offensive_words (offensive_word_text) VALUES
    ('paki'),
    ('panties'),
    ('panty'),
    ('pedobear'),
    ('pedophile'),
    ('pegging'),
    ('penis'),
    ('phone sex'),
    ('piece of shit'),
    ('pissing'),
    ('piss pig'),
    ('pisspig'),
    ('playboy'),
    ('pleasure chest'),
    ('pole smoker'),
    ('ponyplay'),
    ('poof'),
    ('poon'),
    ('poontang'),
    ('punany'),
    ('poop chute'),
    ('poopchute'),
    ('porn'),
    ('porno'),
    ('pornography'),
    ('prince albert piercing'),
    ('pthc'),
    ('pubes'),
    ('pussy'),
    ('queaf'),
    ('queef'),
    ('quim'),
    ('raghead'),
    ('raging boner'),
    ('rape'),
    ('raping'),
    ('rapist'),
    ('rectum'),
    ('reverse cowgirl'),
    ('rimjob'),
    ('rimming'),
    ('rosy palm'),
    ('rosy palm and her 5 sisters'),
    ('rusty trombone'),
    ('sadism'),
    ('santorum'),
    ('scat'),
    ('schlong'),
    ('scissoring'),
    ('semen'),
    ('sex'),
    ('sexo'),
    ('sexy'),
    ('shaved beaver'),
    ('shaved pussy'),
    ('shemale'),
    ('shibari'),
    ('shit'),
    ('shitblimp'),
    ('shitty'),
    ('shota'),
    ('shrimping'),
    ('skeet'),
    ('slanteye'),
    ('slut'),
    ('s&m'),
    ('smut'),
    ('snatch'),
    ('snowballing'),
    ('sodomize'),
    ('sodomy'),
    ('spic'),
    ('splooge'),
    ('splooge moose'),
    ('spooge'),
    ('spread legs'),
    ('spunk'),
    ('strap on'),
    ('strapon'),
    ('strappado'),
    ('strip club'),
    ('style doggy'),
    ('suck'),
    ('sucks'),
    ('suicide girls'),
    ('sultry women'),
    ('swastika'),
    ('swinger'),
    ('tainted love'),
    ('taste my'),
    ('tea bagging'),
    ('threesome'),
    ('throating'),
    ('tied up'),
    ('tight white'),
    ('tit'),
    ('tits'),
    ('titties'),
    ('titty'),
    ('tongue in a'),
    ('topless'),
    ('tosser'),
    ('towelhead'),
    ('tranny'),
    ('tribadism'),
    ('tub girl'),
    ('tubgirl'),
    ('tushy'),
    ('twat'),
    ('twink'),
    ('twinkie'),
    ('two girls one cup'),
    ('undressing'),
    ('upskirt'),
    ('urethra play'),
    ('urophilia'),
    ('vagina'),
    ('venus mound'),
    ('vibrator'),
    ('violet wand'),
    ('vorarephilia'),
    ('voyeur'),
    ('vulva'),
    ('wank'),
    ('wetback'),
    ('wet dream'),
    ('whore'),
    ('white power'),
    ('wrapping men'),
    ('wrinkled starfish'),
    ('xx'),
    ('xxx'),
    ('yaoi'),
    ('yellow showers'),
    ('yiffy'),
    ('zoophilia'),
    ('__');



--
-- Trigger to insert new tuple into table `leaderboard`
--

DROP TRIGGER IF EXISTS `db_marketing_app`.`compute_leaderboard_tuple`;

DELIMITER $$
USE `db_marketing_app`$$
CREATE DEFINER=`root`@`localhost` TRIGGER `compute_leaderboard_tuple` AFTER INSERT ON `answer` FOR EACH ROW BEGIN

IF NOT EXISTS (SELECT * FROM leaderboard WHERE user_id = new.user_id AND product_id = new.product_id) THEN 
INSERT INTO leaderboard (leaderboard_timestamp, daily_points, user_id, product_id)
VALUES (sysdate(), 
(SELECT (1 + question.question_type)
FROM question
WHERE question.question_id = new.question_id), 
new.user_id, 
new.product_id);

ELSEIF EXISTS (SELECT * FROM leaderboard WHERE user_id = new.user_id AND product_id = new.product_id) THEN 
UPDATE leaderboard
SET daily_points = daily_points + (SELECT (1 + question.question_type)
								   FROM question
								   WHERE question.question_id = new.question_id)
WHERE user_id = new.user_id AND product_id = new.product_id;

END IF;

UPDATE User 
SET points = points + (SELECT (1 + question.question_type)
					   FROM question
					   WHERE question.question_id = new.question_id)
WHERE user_id = new.user_id;

END$$
DELIMITER ;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-27 17:56:15
