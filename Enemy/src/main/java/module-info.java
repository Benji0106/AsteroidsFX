module dk.sdu.mmmi.cbse.enemysystem.enemy {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens dk.sdu.mmmi.cbse.enemysystem to javafx.fxml;
    exports dk.sdu.mmmi.cbse.enemysystem;
}