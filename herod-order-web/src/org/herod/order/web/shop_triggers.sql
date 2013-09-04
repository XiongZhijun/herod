DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `HEROD-ORDER`.`ZRH_SHOP_INSERT`$$

CREATE TRIGGER `HEROD-ORDER`.`ZRH_SHOP_INSERT` AFTER INSERT ON `HEROD-ORDER`.`ZRH_SHOP`
FOR EACH ROW BEGIN
UPDATE ZRH_SHOP_TYPE C SET C.TIMESTAMP = NOW() WHERE C.ID = NEW.SHOP_TYPE_ID;
END$$

DROP TRIGGER /*!50032 IF EXISTS */ `HEROD-ORDER`.`ZRH_SHOP_UPDATE`$$

CREATE TRIGGER `HEROD-ORDER`.`ZRH_SHOP_UPDATE` AFTER UPDATE ON `HEROD-ORDER`.`ZRH_SHOP`
FOR EACH ROW BEGIN
UPDATE ZRH_SHOP_TYPE C SET C.TIMESTAMP = NOW() WHERE C.ID = OLD.SHOP_TYPE_ID;
END$$

DROP TRIGGER /*!50032 IF EXISTS */ `HEROD-ORDER`.`ZRH_SHOP_DELETE`$$

CREATE TRIGGER `HEROD-ORDER`.`ZRH_SHOP_DELETE` AFTER DELETE ON `HEROD-ORDER`.`ZRH_SHOP`
FOR EACH ROW BEGIN
UPDATE ZRH_SHOP_TYPE C SET C.TIMESTAMP = NOW() WHERE C.ID = OLD.SHOP_TYPE_ID;
END$$

DELIMITER ;