package com.gndbiz.modules.core.domain.support;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

public class PrefixStrategy extends PhysicalNamingStrategyStandardImpl {

	private static final String PREFIX_NAME = "tb_at_";

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		Identifier prefixedTableName = new Identifier(PREFIX_NAME + name.getText().toLowerCase(), name.isQuoted());
		return super.toPhysicalTableName(prefixedTableName, context);
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
		SpringPhysicalNamingStrategy columnStrategy = new SpringPhysicalNamingStrategy();
		return columnStrategy.toPhysicalColumnName(name, context);
	}
}
