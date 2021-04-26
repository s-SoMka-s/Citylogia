using Microsoft.EntityFrameworkCore.Migrations;

namespace Core.Db.Migrations
{
    public partial class Photos : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "Link",
                schema: "citylogia",
                table: "Photos",
                newName: "PublicUrl");

            migrationBuilder.AlterColumn<long>(
                name: "PlaceId",
                schema: "citylogia",
                table: "Photos",
                type: "bigint",
                nullable: false,
                defaultValue: 0L,
                oldClrType: typeof(long),
                oldType: "bigint",
                oldNullable: true);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "PublicUrl",
                schema: "citylogia",
                table: "Photos",
                newName: "Link");

            migrationBuilder.AlterColumn<long>(
                name: "PlaceId",
                schema: "citylogia",
                table: "Photos",
                type: "bigint",
                nullable: true,
                oldClrType: typeof(long),
                oldType: "bigint");
        }
    }
}
